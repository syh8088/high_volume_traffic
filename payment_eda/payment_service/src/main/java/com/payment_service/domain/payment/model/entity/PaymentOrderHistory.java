package com.payment_service.domain.payment.model.entity;

import com.payment_service.common.CommonEntity;
import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import com.payment_service.domain.payment.model.response.PaymentOrderStatusOutPut;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_order_histories")
public class PaymentOrderHistory extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_order_no")
    private PaymentOrder paymentOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private PaymentOrderStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private PaymentOrderStatus newStatus;

    @Column(name = "reason")
    private String reason;

    @Builder
    private PaymentOrderHistory(PaymentOrder paymentOrder, PaymentOrderStatus previousStatus, PaymentOrderStatus newStatus, String reason) {
        this.paymentOrder = paymentOrder;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }

    public static PaymentOrderHistory of(
            long paymentOrderNo,
            PaymentOrderStatus previousStatus,
            PaymentOrderStatus newStatus,
            String reason
    ) {
        return PaymentOrderHistory.builder()
                .paymentOrder(PaymentOrder.of(paymentOrderNo))
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .reason(reason)
                .build();
    }

    public static List<PaymentOrderHistory> of(
            List<PaymentOrderStatusOutPut> paymentOrderStatusList,
            PaymentOrderStatus newPaymentStatus,
            String reason
    ) {
        return paymentOrderStatusList.stream()
                .map(data -> PaymentOrderHistory.of(data.getNo(), data.getStatus(), newPaymentStatus, reason))
                .toList();
    }

}