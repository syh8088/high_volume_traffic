package com.wallet_service;

import com.wallet_service.domain.wallet.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer implements Consumer<Message<PaymentEventMessage>> {

    private final SettlementService settlementService;

    @SneakyThrows
    @Override
    public void accept(Message<PaymentEventMessage> clusterMessage) {
        log.info("key: {}", clusterMessage.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
        PaymentEventMessage paymentEventMessage = clusterMessage.getPayload();
        settlementService.settlementProcess(paymentEventMessage.orderId());
        log.info("payload: {}", paymentEventMessage);
    }
}