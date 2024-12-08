package com.wallet_service.domain.payment.repository;


import com.wallet_service.domain.payment.model.response.PaymentOrderStatusOutPut;
import com.wallet_service.domain.payment.model.response.PaymentOrderWithSellerOutPut;
import com.wallet_service.domain.payment.model.response.PaymentOrderOutPut;

import java.util.List;

public interface PaymentOrderRepositoryCustom {

    List<PaymentOrderStatusOutPut> selectPaymentOrderStatusListByOrderId(String orderId);

    List<PaymentOrderOutPut> selectPaymentOrderListWithProductByOrderIdList(List<String> orderIdList);

    List<PaymentOrderWithSellerOutPut> selectPaymentOrderListWithSellerByOrderId(String orderId);
}
