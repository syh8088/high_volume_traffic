package com.wallet_service.domain.payment.model.entity;

import com.wallet_service.common.entity.CommonEntity;
import com.wallet_service.domain.payment.enums.PaymentEventMethod;
import com.wallet_service.domain.payment.enums.PaymentEventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_events")
public class PaymentEvent extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @OneToMany(mappedBy = "paymentEvent", fetch = FetchType.LAZY)
    private List<PaymentOrder> paymentOrderList = new ArrayList<>();

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_name")
    private String orderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentEventMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PaymentEventType type;

    @Column(name = "approved_at")
    private LocalDateTime approvedDateTime;

    @Column(name = "psp_raw_data", columnDefinition = "LONGTEXT")
    private String pspRawData;

    @Column(name = "is_payment_done")
    private boolean isPaymentDone;

    @Builder
    private PaymentEvent(List<PaymentOrder> paymentOrderList, String orderId, String paymentKey, String orderName, PaymentEventMethod method, PaymentEventType type, LocalDateTime approvedDateTime, String pspRawData, boolean isPaymentDone) {
        this.paymentOrderList = paymentOrderList;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.orderName = orderName;
        this.method = method;
        this.type = type;
        this.approvedDateTime = approvedDateTime;
        this.pspRawData = pspRawData;
        this.isPaymentDone = isPaymentDone;
    }

}
