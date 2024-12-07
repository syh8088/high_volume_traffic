package com.wallet_service.domain.payment.model.entity;

import com.wallet_service.common.entity.CommonEntity;
import com.wallet_service.domain.payment.enums.PaymentOrderStatus;
import com.wallet_service.domain.seller.model.entity.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_orders")
public class PaymentOrder extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_event_no")
    private PaymentEvent paymentEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_no")
    private Seller seller;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "failed_count")
    private int failedCount;

    @Column(name = "threshold")
    private int threshold;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_order_status")
    private PaymentOrderStatus status;

    @Builder
    private PaymentOrder(Long no, PaymentEvent paymentEvent, Product product, Seller seller, String orderId, BigDecimal amount, int failedCount, int threshold, PaymentOrderStatus status) {
        this.no = no;
        this.paymentEvent = paymentEvent;
        this.product = product;
        this.seller = seller;
        this.orderId = orderId;
        this.amount = amount;
        this.failedCount = failedCount;
        this.threshold = threshold;
        this.status = status;
    }

}
