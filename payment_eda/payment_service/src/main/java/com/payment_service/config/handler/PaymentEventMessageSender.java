package com.payment_service.config.handler;

import com.payment_service.message.model.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 결제 이벤트를 카프카에 실제로 반영하는 역활
 */
@Configuration
@RequiredArgsConstructor
public class PaymentEventMessageSender {

    private final StreamBridge streamBridge;

    private static final String bindingName = "send-out-0";

    @Bean(name = "payment-result")
    public FluxMessageChannel sendResultChannel() {
        return new FluxMessageChannel();
    }

    /**
     * 카프카로 보낸 메시지 전송 결과를 받아볼 수 있도록 한다.
     */
    @ServiceActivator(inputChannel = "payment-result")
    public void receiveSendResult(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload(required = false) PaymentEventMessage paymentEventMessage
    ) {

        System.out.println("paymentEventMessage = " + paymentEventMessage);
    }

    public void dispatch(PaymentEventMessage paymentEventMessage) {
        streamBridge.send(bindingName, MessageBuilder
                .withPayload(paymentEventMessage)
                .setHeader(KafkaHeaders.KEY, paymentEventMessage.getMetadata().get("partitionKey"))
                .build()
        );
    }
}
