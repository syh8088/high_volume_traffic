package com.payment_service.domain.payment.model.response;

import com.payment_service.common.utils.TimeConverter;
import com.payment_service.domain.payment.enums.PSPConfirmationStatus;
import com.payment_service.domain.payment.enums.PaymentEventMethod;
import com.payment_service.domain.payment.enums.PaymentEventType;
import com.payment_service.domain.payment.model.response.toss.TossPaymentConfirmationResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class PaymentExtraDetailsOutPut {

    private PaymentEventMethod method;
    private PaymentEventType type;
    private LocalDateTime approvedAt;
    private String orderName;
    private PSPConfirmationStatus pspConfirmationStatus;
    private BigDecimal totalAmount;

    @Builder
    private PaymentExtraDetailsOutPut(PaymentEventMethod method, PaymentEventType type, LocalDateTime approvedAt, String orderName, PSPConfirmationStatus pspConfirmationStatus, BigDecimal totalAmount) {
        this.method = method;
        this.type = type;
        this.approvedAt = approvedAt;
        this.orderName = orderName;
        this.pspConfirmationStatus = pspConfirmationStatus;
        this.totalAmount = totalAmount;
    }

    public static PaymentExtraDetailsOutPut of(TossPaymentConfirmationResponse tossPaymentConfirmationResponse) {

        if (Objects.isNull(tossPaymentConfirmationResponse)) {
            return null;
        }

        return PaymentExtraDetailsOutPut.builder()
                .method(PaymentEventMethod.getByPaymentEventMethod(tossPaymentConfirmationResponse.getMethod()))
                .type(PaymentEventType.getByPaymentEventType(tossPaymentConfirmationResponse.getType()))
                .approvedAt(TimeConverter.convertStringDateTimeToLocalDateTime(tossPaymentConfirmationResponse.getApprovedAt()))
                .orderName(tossPaymentConfirmationResponse.getOrderName())
                .pspConfirmationStatus(PSPConfirmationStatus.get(tossPaymentConfirmationResponse.getStatus()))
                .totalAmount(BigDecimal.valueOf(tossPaymentConfirmationResponse.getTotalAmount()))
                .build();
    }
}
