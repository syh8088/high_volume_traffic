package com.payment_service.api.payment.service;

import com.payment_service.message.model.PaymentEventMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalEventPublisher;
import reactor.core.publisher.Mono;

@Component
public class PaymentEventMessagePublisher {

    private final TransactionalEventPublisher transactionalEventPublisher;

    public PaymentEventMessagePublisher(ApplicationEventPublisher publisher) {
        this.transactionalEventPublisher = new TransactionalEventPublisher(publisher);
    }

    /**
     * 이 메서드로 발행된 이벤트는 트랜잭션이 성공적으로 커밋될 때까지 지연시킬 수 있습니다.
     * 그러니깐 트랜잭션이 롤백되면 이벤트는 발행되지 않는 겁니다.
     * @param paymentEventMessage
     * @return
     */
    public Mono<PaymentEventMessage> publishEvent(PaymentEventMessage paymentEventMessage) {
        return transactionalEventPublisher.publishEvent(paymentEventMessage)
                .thenReturn(paymentEventMessage);
    }
}