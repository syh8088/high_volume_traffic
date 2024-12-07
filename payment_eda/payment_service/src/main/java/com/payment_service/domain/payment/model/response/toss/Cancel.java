package com.payment_service.domain.payment.model.response.toss;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cancel {
    private int cancelAmount;
    private String cancelReason;
    private int taxFreeAmount;
    private int taxExemptionAmount;
    private int refundableAmount;
    private int easyPayDiscountAmount;
    private String canceledAt;
    private String transactionKey;
    private String receiptKey;
    private boolean isPartialCancelable;
}
