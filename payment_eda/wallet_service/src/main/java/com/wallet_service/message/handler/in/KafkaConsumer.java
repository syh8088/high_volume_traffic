package com.wallet_service.message.handler.in;

import com.wallet_service.domain.wallet.model.WalletEventMessage;
import com.wallet_service.domain.wallet.service.SettlementService;
import com.wallet_service.message.model.PaymentEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer implements Consumer<Message<PaymentEventMessage>> {

    private final SettlementService settlementService;
    private final StreamBridge streamBridge;

    @SneakyThrows
    @Override
    public void accept(Message<PaymentEventMessage> clusterMessage) {

//        throw new Exception("ttt");
        log.info("key: {}", clusterMessage.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
        PaymentEventMessage paymentEventMessage = clusterMessage.getPayload();
        log.info("payload: {}", paymentEventMessage);
        WalletEventMessage walletEventMessage = settlementService.settlementProcess(paymentEventMessage.orderId());

        streamBridge.send("wallet", MessageBuilder.withPayload(walletEventMessage).build());
    }
}