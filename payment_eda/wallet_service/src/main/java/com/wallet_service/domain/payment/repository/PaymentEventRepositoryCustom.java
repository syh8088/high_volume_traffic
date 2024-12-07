package com.wallet_service.domain.payment.repository;


import com.wallet_service.domain.payment.model.response.PaymentEventOutPut;

import java.util.List;

public interface PaymentEventRepositoryCustom {

    List<PaymentEventOutPut> selectPaymentEventList();

}
