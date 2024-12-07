package com.payment_service.api.payment.service;

import com.payment_service.IntegrationTestSupport;
import com.payment_service.api.payment.model.request.PaymentCheckOutRequest;
import com.payment_service.api.payment.model.request.PaymentConfirmRequest;
import com.payment_service.api.payment.model.response.PaymentCheckOutResponse;
import com.payment_service.api.payment.model.response.PaymentEventOutPut;
import com.payment_service.api.payment.model.response.PaymentOrderOutPut;
import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import com.payment_service.domain.payment.model.entity.Product;
import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import com.payment_service.domain.payment.repository.PaymentOrderHistoryRepository;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import com.payment_service.domain.payment.service.PaymentEventQueryService;
import com.payment_service.domain.product.repository.ProductRepository;
import com.payment_service.error.errorCode.PSPErrorCode;
import com.payment_service.error.exception.BusinessException;
import com.payment_service.error.exception.PaymentException;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentConfirmApiServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private PaymentOrderHistoryRepository paymentOrderHistoryRepository;

    @Autowired
    private PaymentCheckOutApiService paymentCheckOutApiService;

    @Autowired
    private PaymentConfirmApiService paymentConfirmApiService;

    @Autowired
    private PaymentEventQueryService paymentEventQueryService;


    @AfterEach
    void tearDown() {
        paymentOrderHistoryRepository.deleteAllInBatch();
        paymentOrderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        paymentEventRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품 주문 하기 위한 Confirm 기능을 실행 합니다.")
    void paymentConfirm() {

        // given
        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000));
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000));
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000));

        List<Product> productList = productRepository.saveAll(List.of(product1, product2, product3));
        List<ProductOutPut> productOutPutList = ProductOutPut.of(productList);
        List<Long> productNoList = productOutPutList.stream()
                .map(ProductOutPut::getProductNo)
                .toList();

        PaymentCheckOutRequest paymentCheckOutRequest = PaymentCheckOutRequest.of(productNoList);
        PaymentCheckOutResponse paymentCheckOutResponse = paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest);

        int intTotalAmount = 6000;
        String totalAmount = String.valueOf(intTotalAmount);

        String paymentKey = this.paymentIdb64uuid();
        PaymentConfirmRequest paymentConfirmRequest
                = PaymentConfirmRequest.of(
                    paymentKey,
                    paymentCheckOutResponse.getOrderId(),
                    totalAmount
                );

        // stubbing
        this.createStubbingTossPostPayment(paymentKey, paymentCheckOutResponse.getOrderId(), totalAmount);

        // when
        paymentConfirmApiService.paymentConfirm(paymentConfirmRequest);

        // then
        List<PaymentEventOutPut> paymentEventOutPutList = paymentEventQueryService.selectPayments();

        assertThat(paymentEventOutPutList).hasSize(1)
                .extracting("paymentKey", "orderId", "isPaymentDone")
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                )
                .containsExactlyInAnyOrder(
                        tuple(paymentKey, paymentCheckOutResponse.getOrderId(), true)
                );

        for (PaymentEventOutPut paymentEventOutPut : paymentEventOutPutList) {

            BigDecimal responseTotalAmount = paymentEventOutPut.getTotalAmount();
            assertThat(responseTotalAmount).isEqualByComparingTo(BigDecimal.valueOf(intTotalAmount));

            List<PaymentOrderOutPut> paymentOrderList = paymentEventOutPut.getPaymentOrderList();

            assertThat(paymentOrderList).hasSize(3)
                    .extracting("productId", "name", "amount", "status")
                    .usingRecursiveFieldByFieldElementComparator(
                            RecursiveComparisonConfiguration.builder()
                                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                    )
                    .containsExactlyInAnyOrder(
                            tuple("AAA", "상품A", BigDecimal.valueOf(1000), PaymentOrderStatus.SUCCESS),
                            tuple("BBB", "상품B", BigDecimal.valueOf(2000), PaymentOrderStatus.SUCCESS),
                            tuple("CCC", "상품C", BigDecimal.valueOf(3000), PaymentOrderStatus.SUCCESS)
                    );
        }
    }

    @Test
    @DisplayName("상품 주문 하기 위한 Confirm 기능을 실행시 경우 예외가 발생 합니다. - 재시도가 가능하지 않는 결제 에러")
    void paymentConfirmThenThrow() {

        // given
        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000));
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000));
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000));

        List<Product> productList = productRepository.saveAll(List.of(product1, product2, product3));
        List<ProductOutPut> productOutPutList = ProductOutPut.of(productList);
        List<Long> productNoList = productOutPutList.stream()
                .map(ProductOutPut::getProductNo)
                .toList();

        PaymentCheckOutRequest paymentCheckOutRequest = PaymentCheckOutRequest.of(productNoList);
        PaymentCheckOutResponse paymentCheckOutResponse = paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest);

        int intTotalAmount = 6000;
        String totalAmount = String.valueOf(intTotalAmount);

        String paymentKey = this.paymentIdb64uuid();
        PaymentConfirmRequest paymentConfirmRequest
                = PaymentConfirmRequest.of(
                paymentKey,
                paymentCheckOutResponse.getOrderId(),
                totalAmount
        );

        // stubbing
        this.createStubbingTossPostPaymentThenThrow();

        // when
        paymentConfirmApiService.paymentConfirm(paymentConfirmRequest);

        // then
        List<PaymentEventOutPut> paymentEventOutPutList = paymentEventQueryService.selectPayments();

        assertThat(paymentEventOutPutList).hasSize(1)
                .extracting("paymentKey", "orderId", "isPaymentDone")
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                )
                .containsExactlyInAnyOrder(
                        tuple(paymentKey, paymentCheckOutResponse.getOrderId(), false)
                );

        for (PaymentEventOutPut paymentEventOutPut : paymentEventOutPutList) {

            BigDecimal responseTotalAmount = paymentEventOutPut.getTotalAmount();
            assertThat(responseTotalAmount).isEqualByComparingTo(BigDecimal.valueOf(intTotalAmount));

            List<PaymentOrderOutPut> paymentOrderList = paymentEventOutPut.getPaymentOrderList();

            assertThat(paymentOrderList).hasSize(3)
                    .extracting("productId", "name", "amount", "status")
                    .usingRecursiveFieldByFieldElementComparator(
                            RecursiveComparisonConfiguration.builder()
                                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                    )
                    .containsExactlyInAnyOrder(
                            tuple("AAA", "상품A", BigDecimal.valueOf(1000), PaymentOrderStatus.FAILURE),
                            tuple("BBB", "상품B", BigDecimal.valueOf(2000), PaymentOrderStatus.FAILURE),
                            tuple("CCC", "상품C", BigDecimal.valueOf(3000), PaymentOrderStatus.FAILURE)
                    );
        }
    }

    @Test
    @DisplayName("상품 주문 하기 위한 Confirm 기능을 실행시 경우 예외가 발생 합니다. - 이미 완료된 결제건")
    void paymentConfirmAlreadyCompletedPaymentThenThrow() {

        // given
        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000));
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000));
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000));

        List<Product> productList = productRepository.saveAll(List.of(product1, product2, product3));
        List<ProductOutPut> productOutPutList = ProductOutPut.of(productList);
        List<Long> productNoList = productOutPutList.stream()
                .map(ProductOutPut::getProductNo)
                .toList();

        PaymentCheckOutRequest paymentCheckOutRequest = PaymentCheckOutRequest.of(productNoList);
        PaymentCheckOutResponse paymentCheckOutResponse = paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest);

        int intTotalAmount = 6000;
        String totalAmount = String.valueOf(intTotalAmount);

        String paymentKey = this.paymentIdb64uuid();
        PaymentConfirmRequest paymentConfirmRequest
                = PaymentConfirmRequest.of(
                paymentKey,
                paymentCheckOutResponse.getOrderId(),
                totalAmount
        );

        // stubbing
        this.createStubbingAlreadyCompletedTossPostPaymentThenThrow(paymentKey, paymentCheckOutResponse.getOrderId(), totalAmount);

        // when
        paymentConfirmApiService.paymentConfirm(paymentConfirmRequest);

        // then
        List<PaymentEventOutPut> paymentEventOutPutList = paymentEventQueryService.selectPayments();

        assertThat(paymentEventOutPutList).hasSize(1)
                .extracting("paymentKey", "orderId", "isPaymentDone")
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                )
                .containsExactlyInAnyOrder(
                        tuple(paymentKey, paymentCheckOutResponse.getOrderId(), true)
                );

        for (PaymentEventOutPut paymentEventOutPut : paymentEventOutPutList) {

            BigDecimal responseTotalAmount = paymentEventOutPut.getTotalAmount();
            assertThat(responseTotalAmount).isEqualByComparingTo(BigDecimal.valueOf(intTotalAmount));

            List<PaymentOrderOutPut> paymentOrderList = paymentEventOutPut.getPaymentOrderList();

            assertThat(paymentOrderList).hasSize(3)
                    .extracting("productId", "name", "amount", "status")
                    .usingRecursiveFieldByFieldElementComparator(
                            RecursiveComparisonConfiguration.builder()
                                    .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                    )
                    .containsExactlyInAnyOrder(
                            tuple("AAA", "상품A", BigDecimal.valueOf(1000), PaymentOrderStatus.SUCCESS),
                            tuple("BBB", "상품B", BigDecimal.valueOf(2000), PaymentOrderStatus.SUCCESS),
                            tuple("CCC", "상품C", BigDecimal.valueOf(3000), PaymentOrderStatus.SUCCESS)
                    );
        }
    }

    @Test
    @DisplayName("결제 금액이 유효하지 않을 경우 예외가 발생 합니다.")
    void invalidPaymentAmountThenThrow() {

        // given
        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000));
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000));
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000));

        List<Product> productList = productRepository.saveAll(List.of(product1, product2, product3));
        List<ProductOutPut> productOutPutList = ProductOutPut.of(productList);
        List<Long> productNoList = productOutPutList.stream()
                .map(ProductOutPut::getProductNo)
                .toList();

        PaymentCheckOutRequest paymentCheckOutRequest = PaymentCheckOutRequest.of(productNoList);
        PaymentCheckOutResponse paymentCheckOutResponse = paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest);

        int intTotalAmount = 5000;
        String totalAmount = String.valueOf(intTotalAmount);

        String paymentKey = this.paymentIdb64uuid();
        PaymentConfirmRequest paymentConfirmRequest
                = PaymentConfirmRequest.of(
                paymentKey,
                paymentCheckOutResponse.getOrderId(),
                totalAmount
        );

        // when // then
        assertThatThrownBy(() -> paymentConfirmApiService.paymentConfirm(paymentConfirmRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("결제 금액이 유효하지 않습니다.");
    }

    private void createStubbingTossPostPayment(String paymentKey, String orderId, String amount) {

        String responsePostPaymentsBody = "{\"mId\":\"tvivarepublica\",\"lastTransactionKey\":\"F69384D0887558A85A78199D534FBBD5\",\"paymentKey\":\"" + paymentKey + "\",\"orderId\":\"" + orderId + "\",\"orderName\":\"상품A 그외\",\"taxExemptionAmount\":0,\"status\":\"DONE\",\"requestedAt\":\"2024-11-30T16:53:34+09:00\",\"approvedAt\":\"2024-11-30T16:53:57+09:00\",\"useEscrow\":false,\"cultureExpense\":false,\"card\":{\"issuerCode\":\"11\",\"acquirerCode\":\"11\",\"number\":\"52361232****606*\",\"installmentPlanMonths\":0,\"isInterestFree\":false,\"interestPayer\":null,\"approveNo\":\"00000000\",\"useCardPoint\":false,\"cardType\":\"신용\",\"ownerType\":\"개인\",\"acquireStatus\":\"READY\",\"amount\":" + amount + "},\"virtualAccount\":null,\"transfer\":null,\"mobilePhone\":null,\"giftCertificate\":null,\"cashReceipt\":null,\"cashReceipts\":null,\"discount\":null,\"cancels\":null,\"secret\":\"ps_GePWvyJnrKvelyyyB2pLVgLzN97E\",\"type\":\"NORMAL\",\"easyPay\":null,\"country\":\"KR\",\"failure\":null,\"isPartialCancelable\":true,\"receipt\":{\"url\":\"https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20241130165334qVdY4&ref=PX\"},\"checkout\":{\"url\":\"https://api.tosspayments.com/v1/payments/tviva20241130165334qVdY4/checkout\"},\"currency\":\"KRW\",\"totalAmount\":" + amount + ",\"balanceAmount\":6000,\"suppliedAmount\":5455,\"vat\":545,\"taxFreeAmount\":0,\"method\":\"카드\",\"version\":\"2022-11-16\",\"metadata\":null}";
        when(tossPaymentClient.paymentConfirm(
                any(String.class), any(PaymentConfirmInPut.class)
        )).thenReturn(ResponseEntity.ok(responsePostPaymentsBody));
    }

    private void createStubbingTossPostPaymentThenThrow() {

        PaymentException paymentException = new PaymentException(
                PSPErrorCode.CLIENT_ERROR,
                "INVALID_STOPPED_CARD",
                new String[]{"정지된 카드 입니다."},
                "{ \"code\": \"INVALID_STOPPED_CARD\", \"message\": \"정지된 카드 입니다.\" }"
        );

        when(tossPaymentClient.paymentConfirm(
                any(String.class), any(PaymentConfirmInPut.class)
        )).thenThrow(paymentException);
    }

    private void createStubbingAlreadyCompletedTossPostPaymentThenThrow(String paymentKey, String orderId, String amount) {

        PaymentException paymentException = new PaymentException(
                PSPErrorCode.CLIENT_ERROR,
                "ALREADY_COMPLETED_PAYMENT",
                new String[]{"이미 완료된 결제 입니다."},
                "{ \"code\": \"ALREADY_COMPLETED_PAYMENT\", \"message\": \"이미 완료된 결제 입니다.\" }"
        );

        when(tossPaymentClient.paymentConfirm(
                any(String.class), any(PaymentConfirmInPut.class)
        )).thenThrow(paymentException);

        String responsePostPaymentsBody = "{\"mId\":\"tvivarepublica\",\"lastTransactionKey\":\"F69384D0887558A85A78199D534FBBD5\",\"paymentKey\":\"" + paymentKey + "\",\"orderId\":\"" + orderId + "\",\"orderName\":\"상품A 그외\",\"taxExemptionAmount\":0,\"status\":\"DONE\",\"requestedAt\":\"2024-11-30T16:53:34+09:00\",\"approvedAt\":\"2024-11-30T16:53:57+09:00\",\"useEscrow\":false,\"cultureExpense\":false,\"card\":{\"issuerCode\":\"11\",\"acquirerCode\":\"11\",\"number\":\"52361232****606*\",\"installmentPlanMonths\":0,\"isInterestFree\":false,\"interestPayer\":null,\"approveNo\":\"00000000\",\"useCardPoint\":false,\"cardType\":\"신용\",\"ownerType\":\"개인\",\"acquireStatus\":\"READY\",\"amount\":" + amount + "},\"virtualAccount\":null,\"transfer\":null,\"mobilePhone\":null,\"giftCertificate\":null,\"cashReceipt\":null,\"cashReceipts\":null,\"discount\":null,\"cancels\":null,\"secret\":\"ps_GePWvyJnrKvelyyyB2pLVgLzN97E\",\"type\":\"NORMAL\",\"easyPay\":null,\"country\":\"KR\",\"failure\":null,\"isPartialCancelable\":true,\"receipt\":{\"url\":\"https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20241130165334qVdY4&ref=PX\"},\"checkout\":{\"url\":\"https://api.tosspayments.com/v1/payments/tviva20241130165334qVdY4/checkout\"},\"currency\":\"KRW\",\"totalAmount\":" + amount + ",\"balanceAmount\":6000,\"suppliedAmount\":5455,\"vat\":545,\"taxFreeAmount\":0,\"method\":\"카드\",\"version\":\"2022-11-16\",\"metadata\":null}";
        when(tossPaymentClient.getPayments(
                any(String.class)
        )).thenReturn(ResponseEntity.ok(responsePostPaymentsBody));
    }

    private Product createProduct(String productId, String name, BigDecimal price) {

        return Product.of(productId, name, price);
    }

    private String paymentIdb64uuid() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[25];
        secureRandom.nextBytes(randomBytes);
        String identifier = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return identifier.substring(0, 30);
    }
}