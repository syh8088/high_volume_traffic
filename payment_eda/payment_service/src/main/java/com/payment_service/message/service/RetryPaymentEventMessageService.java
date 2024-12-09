package com.payment_service.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.message.model.PaymentEventMessage;
import com.payment_service.message.enums.PaymentEventMessageType;
import com.payment_service.config.handler.PaymentEventMessageSender;
import com.payment_service.domain.outbox.model.response.OutBoxOutPut;
import com.payment_service.domain.outbox.service.OutBoxQueryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * outbox 에 저장된 이벤트 메시지를 출력해 이벤트 메시지 발행 역활
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetryPaymentEventMessageService {

    private final OutBoxQueryService outBoxQueryService;
    private final PaymentEventMessageSender paymentEventMessageSender;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 전송되지 않은 메시지들을 가지고 와서 카프카로 전송하는 릴레이 함수
     */
    @SneakyThrows
    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
    public void eventMessageRelay() {

        List<OutBoxOutPut> outBoxList = outBoxQueryService.selectOutBoxPendingEventMessageList();
        for (OutBoxOutPut outBoxOutPut : outBoxList) {

            PaymentEventMessage paymentEventMessage = PaymentEventMessage.of(
                    PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
                    objectMapper.readValue(outBoxOutPut.getPayload(), Map.class),
                    objectMapper.readValue(outBoxOutPut.getMetadata(), Map.class)
            );
            paymentEventMessageSender.dispatch(paymentEventMessage);
        }
    }
}
