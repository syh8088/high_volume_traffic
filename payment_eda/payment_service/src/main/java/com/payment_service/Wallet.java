package com.payment_service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class Wallet implements Consumer<Message<WalletEventMessage>> {

    private final StreamBridge streamBridge;

    @SneakyThrows
    @Override
    public void accept(Message<WalletEventMessage> clusterMessage) {

//        throw new Exception("ttt");
        log.info("key: {}", clusterMessage.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
        WalletEventMessage walletEventMessage = clusterMessage.getPayload();
        log.info("payload: {}", clusterMessage);
    }
}