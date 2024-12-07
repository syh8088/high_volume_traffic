package com.payment_service.api.payment.service;

import com.payment_service.IntegrationTestSupport;
import com.payment_service.api.payment.model.request.PaymentCheckOutRequest;
import com.payment_service.api.payment.model.response.PaymentCheckOutResponse;
import com.payment_service.common.IdempotencyCreator;
import com.payment_service.domain.payment.model.entity.Product;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import com.payment_service.domain.product.repository.ProductRepository;
import com.payment_service.error.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PaymentCheckOutApiServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private PaymentCheckOutApiService paymentCheckOutApiService;

    @AfterEach
    void tearDown() {
        paymentOrderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        paymentEventRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품 주문 하기 위한 CheckOut 기능을 실행 합니다.")
    void paymentCheckOut() {

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

        // when
        PaymentCheckOutResponse paymentCheckOutResponse = paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest);

        // then
        assertThat(paymentCheckOutResponse.getOrderId()).isNotNull();

        String idempotency = IdempotencyCreator.create(paymentCheckOutRequest);
        assertThat(paymentCheckOutResponse.getOrderId()).isEqualTo(idempotency);

        assertThat(paymentCheckOutResponse.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(6000));
    }

    @Test
    @DisplayName("상품이 존재하지 않는 상품을 주문 위한 CheckOut 기능을 실행시 경우 예외가 발생한다.")
    void paymentCheckOutNoProduct() {

        // given
        List<Long> productNoList = List.of(1L, 2L, 3L);
        PaymentCheckOutRequest paymentCheckOutRequest = PaymentCheckOutRequest.of(productNoList);

        // when // then
        assertThatThrownBy(() -> paymentCheckOutApiService.paymentCheckOut(paymentCheckOutRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    private Product createProduct(String productId, String name, BigDecimal price) {

        return Product.of(productId, name, price);
    }

}