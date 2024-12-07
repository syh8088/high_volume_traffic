package com.payment_service.domain.payment.model.response;

import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import com.payment_service.domain.payment.model.response.toss.TossFailureResponse;
import com.payment_service.domain.payment.model.response.toss.TossPaymentConfirmationResponse;
import com.payment_service.domain.payment.model.response.toss.TossPaymentConfirmationWithPspRawDataResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PaymentExecutionResultOutPut {

    private String paymentKey;
    private String orderId;
    private PaymentExtraDetailsOutPut extraDetails = null;
    private PaymentFailureOutPut failure = null;
    private boolean isSuccess;
    private boolean isFailure;
    private boolean isUnknown;
    private boolean isRetryable;
    private String pspRawData;
    private PaymentOrderStatus paymentStatus;

    @Builder
    private PaymentExecutionResultOutPut(String paymentKey, String orderId, PaymentExtraDetailsOutPut extraDetails, PaymentFailureOutPut failure, boolean isSuccess, boolean isFailure, boolean isUnknown, boolean isRetryable, String pspRawData, PaymentOrderStatus paymentStatus) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.extraDetails = extraDetails;
        this.failure = failure;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryable = isRetryable;
        this.pspRawData = pspRawData;
        this.paymentStatus = paymentStatus;
    }

    public static PaymentExecutionResultOutPut of(PaymentConfirmInPut paymentConfirmInPut, TossPaymentConfirmationWithPspRawDataResponse response) {

        TossPaymentConfirmationResponse tossPaymentConfirmationResponse = response.getTossPaymentConfirmationResponse();
        TossFailureResponse tossFailureResponse = response.getTossFailureResponse();
        PaymentFailureOutPut paymentFailureOutPut = PaymentFailureOutPut.of(tossFailureResponse);

        boolean isSuccess;
        boolean isFailure;
        boolean isUnknown;
        boolean isRetryable;

        if (!Objects.isNull(tossFailureResponse)) {
            isSuccess = tossFailureResponse.isSuccess();
            isFailure = tossFailureResponse.isFailure();
            isUnknown = tossFailureResponse.isUnknown();
            isRetryable = tossFailureResponse.isRetryableError();
        }
        else {
            isSuccess = true;
            isFailure = false;
            isUnknown = false;
            isRetryable = false;
        }

        PaymentExtraDetailsOutPut paymentExtraDetailsOutPut = PaymentExtraDetailsOutPut.of(tossPaymentConfirmationResponse);

        PaymentExecutionResultOutPut paymentExecutionResultOutPut = PaymentExecutionResultOutPut.builder()
                .paymentKey(paymentConfirmInPut.getPaymentKey())
                .orderId(paymentConfirmInPut.getOrderId())
                .extraDetails(paymentExtraDetailsOutPut)
                .failure(paymentFailureOutPut)
                .isSuccess(isSuccess)
                .isFailure(isFailure)
                .isUnknown(isUnknown)
                .isRetryable(isRetryable)
                .pspRawData(response.getPspRawData())
                .build();

        paymentExecutionResultOutPut.updatePaymentStatus();

        return paymentExecutionResultOutPut;
    }

    private void updatePaymentStatus() {

        if (isSuccess) {
            this.paymentStatus = PaymentOrderStatus.SUCCESS;
        }
        else if (isFailure) {
            this.paymentStatus = PaymentOrderStatus.FAILURE;
        }
        else if (isUnknown) {
            this.paymentStatus = PaymentOrderStatus.UNKNOWN;
        }
        else {
            throw new IllegalStateException("결제 (orderId: " + orderId + ") 는 올바르지 않은 결제 상태입니다.");
        }
    }
}
