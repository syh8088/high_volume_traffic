package com.wallet_service;

import com.wallet_service.domain.payment.enums.PaymentEventMethod;
import com.wallet_service.domain.payment.enums.PaymentEventType;
import com.wallet_service.domain.payment.enums.PaymentOrderStatus;
import com.wallet_service.domain.payment.model.entity.PaymentEvent;
import com.wallet_service.domain.payment.model.entity.PaymentOrder;
import com.wallet_service.domain.payment.repository.PaymentEventRepository;
import com.wallet_service.domain.payment.repository.PaymentOrderRepository;
import com.wallet_service.domain.product.model.entity.Product;
import com.wallet_service.domain.product.model.response.ProductOutPut;
import com.wallet_service.domain.product.repository.ProductRepository;
import com.wallet_service.domain.seller.model.entity.Seller;
import com.wallet_service.domain.seller.repository.SellerRepository;
import com.wallet_service.domain.wallet.model.entity.Wallet;
import com.wallet_service.domain.wallet.repository.WalletRepository;
import com.wallet_service.domain.wallet.service.SettlementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles("test")
@SpringBootTest
class KafkaConsumerTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Test
    @DisplayName("정산 시스템 진행시 race condition 문제가 발생 될수 있어서 낙관적 락 (Optimistic-Lock) 이용해서 OptimisticLockingFailureException 발생시 Retry 할 수 있도록 합니다.")
    void settlementProcess() throws InterruptedException {

        // given
        Seller sellerA = Seller.of("sellerA");
        Seller save = sellerRepository.save(sellerA);

        Wallet walletA = Wallet.of(sellerA, BigDecimal.ZERO);
        walletRepository.save(walletA);

        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000), sellerA);
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000), sellerA);
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000), sellerA);

        List<Product> productList = productRepository.saveAll(List.of(product1, product2, product3));
        List<ProductOutPut> productOutPutList = ProductOutPut.of(productList, save.getNo());

        String paymentKey1 = "paymentKey1";
        String orderId1 = "orderId1";

        this.createPayment(paymentKey1, orderId1, List.of(product1, product2, product3), sellerA);

        String paymentKey2 = "paymentKey2";
        String orderId2 = "orderId2";

        this.createPayment(paymentKey2, orderId2, List.of(product1), sellerA);

        String paymentKey3 = "paymentKey3";
        String orderId3 = "orderId3";

        this.createPayment(paymentKey3, orderId3, List.of(product1, product2), sellerA);

        String[] orderIds = new String[]{"orderId1", "orderId2", "orderId3"};

        // when
        int threadCount = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    String orderId = orderIds[finalI];
                    settlementService.settlementProcess(orderId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        Optional<Wallet> optionalWallet = walletRepository.findById(walletA.getNo());
        if (optionalWallet.isPresent()) {
            Wallet wallet = optionalWallet.get();
            assertThat(wallet.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(10000));
        }
        else {
            fail("Wallet not exist");
        }
    }

    private void createPayment(String paymentKey, String orderId, List<Product> productList, Seller sellerA) {
        PaymentEvent paymentEvent = PaymentEvent.of(paymentKey, orderId, "TESTA", PaymentEventMethod.CARD, PaymentEventType.NORMAL);
        paymentEventRepository.save(paymentEvent);

        for (Product product : productList) {
            this.createPaymentOrder(paymentEvent, product, sellerA, PaymentOrderStatus.SUCCESS);
        }
    }

    private void createPaymentOrder(
            PaymentEvent paymentEvent,
            Product product,
            Seller seller,
            PaymentOrderStatus status
    ) {
        PaymentOrder paymentOrder = PaymentOrder.of(paymentEvent, product, seller, status);
        paymentOrderRepository.save(paymentOrder);
    }

    private Product createProduct(String productId, String name, BigDecimal price, Seller seller) {

        return Product.of(productId, name, price, seller);
    }
}