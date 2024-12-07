package com.payment_service.domain.payment.repository;

import com.payment_service.api.payment.model.response.PaymentEventOutPut;

import java.util.List;

public interface PaymentEventRepositoryCustom {

    List<PaymentEventOutPut> selectPaymentEventList();

}
