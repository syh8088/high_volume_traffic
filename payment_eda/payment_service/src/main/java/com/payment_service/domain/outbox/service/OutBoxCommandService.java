package com.payment_service.domain.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.api.payment.controller.PaymentEventMessage;
import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.model.entity.OutBox;
import com.payment_service.domain.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OutBoxCommandService {

    private final OutBoxRepository outBoxRepository;

    @SneakyThrows
    public void insertOutBox(PaymentEventMessage paymentEventMessage)  {

        ObjectMapper objectMapper = new ObjectMapper();

        OutBox outBox = OutBox.of(
                OutBoxStatus.INIT,
                (String) paymentEventMessage.getPayload().get("orderId"),
                paymentEventMessage.getType().name(),
                (paymentEventMessage.getMetadata().get("partitionKey") != null) ? (int) paymentEventMessage.getMetadata().get("partitionKey") : 0,
                objectMapper.writeValueAsString(paymentEventMessage.getPayload()),
                objectMapper.writeValueAsString(paymentEventMessage.getMetadata())

        );

        outBoxRepository.save(outBox);
    }
}
