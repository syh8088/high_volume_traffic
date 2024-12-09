package com.payment_service.message.handler.in;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.service.OutBoxCommandService;
import com.payment_service.domain.payment.service.PaymentEventCommendService;
import com.payment_service.message.model.WalletEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service("wallet")
@RequiredArgsConstructor
public class WalletKafkaConsumer implements Consumer<Message<WalletEventMessage>> {

    private final StreamBridge streamBridge;
    private final PaymentEventCommendService paymentEventCommendService;
    private final OutBoxCommandService outBoxCommandService;

    @SneakyThrows
    @Override
    public void accept(Message<WalletEventMessage> clusterMessage) {

        log.info("key: {}", clusterMessage.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
        log.info("payload: {}", clusterMessage);
        WalletEventMessage walletEventMessage = clusterMessage.getPayload();
        String orderId = walletEventMessage.orderId();

        paymentEventCommendService.updateIsWalletDoneByOrderId(orderId, true);
        outBoxCommandService.updateStatusByIdempotencyKey(orderId, OutBoxStatus.SUCCESS);
    }
}