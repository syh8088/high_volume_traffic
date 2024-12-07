package com.payment_service.domain.payment.model.response.toss;

import com.payment_service.error.exception.PSPConfirmationException;
import com.payment_service.error.exception.PaymentException;
import lombok.*;

@Getter
@NoArgsConstructor
public class TossFailureResponse {

    private String code;
    private String errorCode;
    private String message;
    private boolean isSuccess; // 성공한 결제 요청인지 여부
    private boolean isFailure; // 명백한 결제 실패인지 여부
    private boolean isUnknown; // 알수 없는 상태인지 여부
    private boolean isRetryableError; // 재시도가 가능한 상태인지 여부

    @Builder
    private TossFailureResponse(String code, String errorCode, String message, boolean isSuccess, boolean isFailure, boolean isUnknown, boolean isRetryableError) {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryableError = isRetryableError;
    }

    public static TossFailureResponse of(Exception exception) {

        if (exception instanceof PaymentException) {
            PaymentException paymentException = (PaymentException) exception;
            return TossFailureResponse.builder()
                    .code(paymentException.getErrorCode().getCode())
                    .errorCode(paymentException.getPspErrorCode())
                    .message(paymentException.getMessage())
                    .isSuccess(false)
                    .isFailure(true)
                    .isUnknown(false)
                    .isRetryableError(false)
                    .build();
        }

        if (exception instanceof PSPConfirmationException) {
            PSPConfirmationException pspConfirmationException = (PSPConfirmationException) exception;
            return TossFailureResponse.builder()
                    .code(pspConfirmationException.getErrorCode())
                    .errorCode(pspConfirmationException.getErrorCode())
                    .message(pspConfirmationException.getErrorMessage())
                    .isSuccess(pspConfirmationException.isSuccess())
                    .isFailure(pspConfirmationException.isFailure())
                    .isUnknown(pspConfirmationException.isUnknown())
                    .isRetryableError(pspConfirmationException.isRetryableError())
                    .build();
        }

        return null;
    }
}
