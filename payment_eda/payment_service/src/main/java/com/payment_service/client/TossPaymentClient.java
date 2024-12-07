package com.payment_service.client;


import com.payment_service.config.feign.TossPaymentFeignConfig;
import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "tossPayment",
        url = "${toss.url}",
        configuration = {
                TossPaymentFeignConfig.class,
        }
)
public interface TossPaymentClient {

    /**
     * <h1>신용카드 - (결제) 승인신청 API</h1> </br>
     *
     * @author hun
     * @version 1.0.0
     * @date 2024/06/19
     **/
    @PostMapping(value = "/v1/payments/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> paymentConfirm(@RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody final PaymentConfirmInPut request);

    /**
     * <h1>신용카드 - (결제) 승인신청 API - 강제 에러 발생 전용 API</h1> </br>
     **/
    @PostMapping(value = "/v1/payments/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> paymentError(@RequestHeader("Idempotency-Key") String idempotencyKey, @RequestHeader("TossPayments-Test-Code") String testErrorCode, @RequestBody final PaymentConfirmInPut request);


    /**
     * <h1>신용카드 - 승인조회 API</h1> </br>
     **/
    @GetMapping(value = "/v1/payments/{paymentKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getPayments(@PathVariable("paymentKey") final String paymentKey);


    /**
     * <h1>신용카드 - 승인취소 API</h1> </br>
     *
     * @author hun
     * @version 1.0.0
     * @date 2024/06/19
     **/
    @PostMapping(value = "/v1/payments/card/{transactionId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> postCancelPayments(@PathVariable("transactionId") final String transactionId);

    /**
     * <h1>신용카드 - 승인부분취소 API</h1> </br>
     *
     * @author hun
     * @version 1.0.0
     * @date 2024/06/19
     **/
//    @PostMapping(value = "/v1/payments/card/{transactionId}/cancel-part", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<String> postCancelPartPayments(@PathVariable("transactionId") final String transactionId, @RequestBody HyosungPaymentCancelPartRequest hyosungPaymentCancelPartRequest);

}