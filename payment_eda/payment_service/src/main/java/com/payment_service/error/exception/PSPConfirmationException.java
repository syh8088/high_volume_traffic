package com.payment_service.error.exception;

import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PSPConfirmationException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private boolean isSuccess; // 성공한 결제 요청인지 여부
    private boolean isFailure; // 명백한 결제 실패인지 여부
    private boolean isUnknown; // 알수 없는 상태인지 여부
    private boolean isRetryableError; // 재시도가 가능한 상태인지 여부

    public PSPConfirmationException(String errorCode, String errorMessage, boolean isSuccess, boolean isFailure, boolean isUnknown, boolean isRetryableError) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryableError = isRetryableError;

        /**
         * PSPConfirmationException 객체가 올바른 상태로 만들어졌는지 확인하는 검증하는 로직
         */
        if (!(isSuccess || isFailure || isUnknown)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " 는 올바르지 않은 결제 상태를 가지고 있습니다.");
        }
    }

    public PaymentOrderStatus paymentStatus() {

        if (isSuccess) {
            return PaymentOrderStatus.SUCCESS;
        }
        else if (isFailure) {
            return PaymentOrderStatus.FAILURE;
        }
        else if (isUnknown) {
            return PaymentOrderStatus.UNKNOWN;
        }
        else {
            throw new IllegalStateException(this.getClass().getSimpleName() + " 는 올바르지 않은 결제 상태를 가지고 있습니다.");
        }
    }

    public String toJson() {
        return "{\"code\" : " + this.getErrorCode() + ", \"message\"" + this.getErrorMessage() + "}";
    }
}