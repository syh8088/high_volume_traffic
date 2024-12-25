package com.payment_service.message.handler;

import com.payment_service.message.model.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class PaymentApplicationEventListener {

    private final StreamBridge streamBridge;
    private static final String bindingName = "send-out-0";

    /**
     * 이 메서드로 발행된 이벤트는 트랜잭션이 성공적으로 커밋될 때까지 지연시킬 수 있습니다.
     * 그러니깐 트랜잭션이 롤백되면 이벤트는 발행되지 않는 겁니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(PaymentEventMessage paymentEventMessage) {

        if (!Objects.isNull(paymentEventMessage)) {
            streamBridge.send(bindingName, MessageBuilder
                    .withPayload(paymentEventMessage)
                    .setHeader(KafkaHeaders.KEY, paymentEventMessage.getMetadata().get("partitionKey"))
                    .build()
            );
        }
    }
}
