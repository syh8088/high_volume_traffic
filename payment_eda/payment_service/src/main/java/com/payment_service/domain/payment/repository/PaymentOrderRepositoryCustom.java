package com.payment_service.domain.payment.repository;

import com.payment_service.api.payment.model.response.PaymentOrderOutPut;
import com.payment_service.domain.payment.model.response.PaymentOrderStatusOutPut;

import java.util.List;

public interface PaymentOrderRepositoryCustom {

    List<PaymentOrderStatusOutPut> selectPaymentOrderStatusListByOrderId(String orderId);

    List<PaymentOrderOutPut> selectPaymentOrderListWithProductByOrderIdList(List<String> orderIdList);
}
