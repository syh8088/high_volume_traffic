package com.payment_service.domain.outbox.model.entity;

import com.payment_service.common.CommonEntity;
import com.payment_service.domain.outbox.enums.OutBoxStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox")
public class OutBox extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OutBoxStatus status;

    private String idempotencyKey;

    private String type;

    private Integer partitionKey;

    @Column(name = "payload", columnDefinition = "LONGTEXT")
    private String payload;

    @Column(name = "metadata", columnDefinition = "LONGTEXT")
    private String metadata;

    @Builder
    private OutBox(OutBoxStatus status, String idempotencyKey, String type, Integer partitionKey, String payload, String metadata) {
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.type = type;
        this.partitionKey = partitionKey;
        this.payload = payload;
        this.metadata = metadata;
    }

    public static OutBox of(OutBoxStatus status, String idempotencyKey, String type, Integer partitionKey, String payload, String metadata) {
        return OutBox.builder()
                .status(status)
                .idempotencyKey(idempotencyKey)
                .type(type)
                .partitionKey(partitionKey)
                .payload(payload)
                .metadata(metadata)
                .build();
    }

}
