package com.payment_service.domain.outbox.model.response;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutBoxOutPut {

    private long no;
    private OutBoxStatus status;
    private String idempotencyKey;
    private String type;
    private Integer partitionKey;
    private String payload;
    private String metadata;

    @QueryProjection
    public OutBoxOutPut(long no, OutBoxStatus status, String idempotencyKey, String type, Integer partitionKey, String payload, String metadata) {
        this.no = no;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.type = type;
        this.partitionKey = partitionKey;
        this.payload = payload;
        this.metadata = metadata;
    }
}
