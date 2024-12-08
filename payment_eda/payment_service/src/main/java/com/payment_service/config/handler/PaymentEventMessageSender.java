package com.payment_service.config.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.messaging.MessageHandler;
import reactor.core.publisher.Sinks;

/**
 * 결제 이벤트를 카프카에 실제로 반영하는 역활
 */
@Configuration
@RequiredArgsConstructor
public class PaymentEventMessageSender {


//    @Bean(name = "payment-result")
//    public FluxMessageChannel sendResultChannel() {
//        return new FluxMessageChannel();
//    }

    /**
     * 카프카로 보낸 메시지 전송 결과를 받아볼 수 있도록 한다.
     * @param results
     */
//    @ServiceActivator(inputChannel = "payment-result")
//    public MessageHandler sysoutHandler() {
//        return message -> System.out.println("message payload: " + message.getPayload());
//    }

//    public void receiveSendResult(SenderResult<String> results) {
//        if (results.exception() != null) {
//            System.out.println("results = " + results);
////			LoggerImpl.error("sendEventMessage", results.exception().getMessage() != null ? "receive an exception for event message send." : results.exception().getMessage(), results.exception());
//        }
//
//        sendResult.emitNext(results, Sinks.EmitFailureHandler.FAIL_FAST);
//    }
}
