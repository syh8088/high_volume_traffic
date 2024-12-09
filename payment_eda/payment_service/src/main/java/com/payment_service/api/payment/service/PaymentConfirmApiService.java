package com.payment_service.api.payment.service;

import com.payment_service.message.model.PaymentEventMessage;
import com.payment_service.api.payment.model.request.PaymentConfirmRequest;
import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import com.payment_service.domain.payment.model.response.PaymentExecutionResultOutPut;
import com.payment_service.domain.payment.service.toss.TossPaymentExecutor;
import com.payment_service.domain.payment.validator.PaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@RequiredArgsConstructor
public class PaymentConfirmApiService {

    private final PaymentValidator paymentValidator;
    private final TossPaymentExecutor tossPaymentExecutor;
    private final PaymentStatusUpdateApiService paymentStatusUpdateApiService;
    private final StreamBridge streamBridge;

    private static final String bindingName = "send-out-0";

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void paymentConfirm(PaymentConfirmRequest request) {

        paymentValidator.paymentTotalAmountValidation(request.getOrderId(), request.getAmount());

        PaymentConfirmInPut paymentConfirmInPut = PaymentConfirmInPut.of(request.getPaymentKey(), request.getOrderId(), request.getAmount());
        PaymentExecutionResultOutPut paymentExecutionResult = tossPaymentExecutor.paymentConfirm(paymentConfirmInPut);

        PaymentEventMessage paymentEventMessage = paymentStatusUpdateApiService.updatePaymentStatus(paymentExecutionResult);

        if (!Objects.isNull(paymentEventMessage)) {
            streamBridge.send(bindingName, MessageBuilder
                    .withPayload(paymentEventMessage)
                    .setHeader(KafkaHeaders.KEY, paymentEventMessage.getMetadata().get("partitionKey"))
                    .build()
            );
        }
    }
}
