package com.payment_service.config.feign.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.payment_service.common.utils.JsonUtils;
import com.payment_service.config.feign.FeignResponseUtils;
import com.payment_service.domain.payment.enums.TossPaymentError;
import com.payment_service.domain.payment.model.response.toss.TossFailureResponse;
import com.payment_service.error.errorCode.PSPErrorCode;
import com.payment_service.error.exception.PSPConfirmationException;
import com.payment_service.error.exception.PaymentException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class TossPaymentFeignErrorDecode implements ErrorDecoder {

    private final Gson gson = JsonUtils.getPrettyGson();

    @Override
    public Exception decode(String methodKey, Response response) {
        final HttpStatus.Series series = HttpStatus.valueOf(response.status()).series();

        final String requestBody = FeignResponseUtils.getRequestBody(response);
        final String responseBody = FeignResponseUtils.getResponseBody(response);

        this.retryPaymentExceptionCheck(responseBody);
        this.payment4xxExceptionCheck(response.status(), responseBody);

        final JsonObject requestBodyJson = stringToJson(requestBody);
        final JsonObject responseBodyJson = stringToJson(responseBody);

        log.error(":: 결제 오류 메소드                     \t =>    {} ::", methodKey);
        log.info(":: [API Server -> Toss] Request       \t => \n {} ::", gson.toJson(requestBodyJson));
        log.info(":: [API Server <- Toss] Response      \t => \n {} ::", gson.toJson(responseBodyJson));

        final TossFailureResponse paymentErrorResponse = gson.fromJson(responseBodyJson, TossFailureResponse.class);

        switch (series) {
            case CLIENT_ERROR:
                log.error(":: [클라이언트] 결제 오류 URI     \t => {} ::", response.request().url());
                log.error(":: [클라이언트] 결제 오류 내용     \t => {} ::", paymentErrorResponse.getMessage());
                log.error(":: [클라이언트] 결제 오류 코드     \t => {} ::", paymentErrorResponse.getCode());

                throw new PaymentException(PSPErrorCode.CLIENT_ERROR, paymentErrorResponse.getCode(), new String[]{paymentErrorResponse.getMessage()}, responseBody);

            case SERVER_ERROR:
                log.error(":: [서버]     결제 요청 URI     \t => {} ::", response.request().url());
                log.error(":: [서버]     결제 오류 내용     \t => {} ::", paymentErrorResponse.getMessage() );
                log.error(":: [서버]     결제 오류 코드     \t => {} ::", paymentErrorResponse.getCode());

                throw new PaymentException(PSPErrorCode.SERVER_ERROR, paymentErrorResponse.getCode(), new String[]{paymentErrorResponse.getMessage()}, responseBody);

            default:
                log.error(":: [서버]     결제 요청 Default \t => {} ::", "정의되지 않은 오류.");
                log.error(":: [서버]     결제 요청 URI     \t => {} ::", response.request().url());
                log.error(":: [서버]     결제 오류 내용     \t => {} ::", paymentErrorResponse.getMessage() );
                log.error(":: [서버]     결제 오류 코드     \t => {} ::", paymentErrorResponse.getCode());

                throw new PaymentException(PSPErrorCode.SERVER_ERROR, paymentErrorResponse.getCode(), new String[]{paymentErrorResponse.getMessage()}, responseBody);
        }
    }

    /**
     * 토스 페이먼트는 실패한 응답을 400번이나 500번과 같은 상태 코드로 함께 응답을
     * 전송한다. 이 응답 에러를 분석해서 잔액 부족과 같이 재시도가 불가능한 에러인지 일시적
     * 오류와 같은 재시도가 가능한 에러인지 판단해서 재시도 한다.
     */
    private void retryPaymentExceptionCheck(String responseBody) {

        if (StringUtils.isNotBlank(responseBody)) {
            final JsonObject jsonObject = JsonUtils.fromJson(responseBody, JsonObject.class);
            String responseJson = jsonObject.toString();
            TossFailureResponse tossFailureResponse = JsonUtils.fromJson(responseJson, TossFailureResponse.class);
            TossPaymentError error = TossPaymentError.get(tossFailureResponse.getCode());

            if (error.isRetryableError()) {
                throw new PSPConfirmationException(
                        error.name(),
                        error.getDescription(),
                        error.isSuccess(),
                        error.isFailure(),
                        error.isUnknown(),
                        error.isRetryableError()
                );
            }
        }
    }

    private void payment4xxExceptionCheck(int responseStatusCode, String responseBody) {
        final HttpStatus responseStatus = HttpStatus.valueOf(responseStatusCode);

        if ((HttpStatus.UNAUTHORIZED == responseStatus) && StringUtils.isBlank(responseBody)) {
            throw new PaymentException(PSPErrorCode.CLIENT_ERROR, new String[]{"접근 권한이 없습니다."});
        }

        if (responseStatus.is4xxClientError() && StringUtils.isBlank(responseBody)) {
            throw new PaymentException(PSPErrorCode.CLIENT_ERROR, new String[]{"PG사의 응답데이터가 정상적이지 않습니다."});
        }
    }

    private JsonObject stringToJson(String json) {
        try {
            return gson.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Getter
    @ToString
    static class PaymentErrorResponse {
        private String message;
        private String developerMessage;
    }
}