package com.payment_service.api.payment.controller;

import com.payment_service.api.payment.model.response.PaymentEventOutPut;
import com.payment_service.api.payment.model.response.PaymentEventResponse;
import com.payment_service.api.payment.model.response.PaymentEventWithOrderResponse;
import com.payment_service.api.payment.service.PaymentApiService;
import com.payment_service.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentApiService paymentApiService;

    @GetMapping
    public ApiResponse<PaymentEventWithOrderResponse> selectPayments() {

        List<PaymentEventOutPut> paymentEventOutPutList = paymentApiService.selectPayments();
        List<PaymentEventResponse> paymentEventResponses = PaymentEventResponse.of(paymentEventOutPutList);
        PaymentEventWithOrderResponse paymentEventWithOrderResponse = PaymentEventWithOrderResponse.of(paymentEventResponses);

        return ApiResponse.ok(paymentEventWithOrderResponse);
    }

    private final StreamBridge streamBridge;
    private final PartitionKeyUtil partitionKeyUtil;
    private static final String bindingName = "send-out-0";

    @GetMapping("test")
    public String test() {

        String orderId = "tewkgmwepm2394234" + LocalDateTime.now().toString();
        int partitionKey = partitionKeyUtil.createPartitionKey(orderId.hashCode());
        PaymentEventMessage paymentEventMessage = this.createPaymentEventMessage(orderId, partitionKey);

//        streamBridge.send(bindingName, MessageBuilder.withPayload(paymentEventMessage).build());
//
        streamBridge.send(bindingName, MessageBuilder
                .withPayload(paymentEventMessage)
                .setHeader(KafkaHeaders.KEY, String.valueOf(partitionKey))
                .build());

        return "test";
    }

    private PaymentEventMessage createPaymentEventMessage(String orderId, int partitionKey) {
        return PaymentEventMessage.of(
                PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                Map.of("orderId", orderId),
                Map.of("partitionKey", partitionKey)
        );
    }
}
