package com.payment_service.message.model;

import com.payment_service.message.enums.PaymentEventMessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class PaymentEventMessage {

    private PaymentEventMessageType type;
    private Map<String, Object> payload;
    private Map<String, Object> metadata;

    private PaymentEventMessage(PaymentEventMessageType type, Map<String, Object> payload, Map<String, Object> metadata) {
        this.type = type;
        this.payload = payload;
        this.metadata = metadata;
    }

    public static PaymentEventMessage of(PaymentEventMessageType type, Map<String, Object> payload, Map<String, Object> metadata) {
        return new PaymentEventMessage(type, payload, metadata);
    }


}