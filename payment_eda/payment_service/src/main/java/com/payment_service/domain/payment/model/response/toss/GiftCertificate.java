package com.payment_service.domain.payment.model.response.toss;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate {
    private String approveNo;
    private String settlementStatus;
}