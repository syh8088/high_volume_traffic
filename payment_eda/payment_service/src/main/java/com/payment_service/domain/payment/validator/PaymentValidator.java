package com.payment_service.domain.payment.validator;

import com.payment_service.domain.payment.service.PaymentOrderQueryService;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.error.errorCode.PaymentErrorCode;
import com.payment_service.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentValidator {

    private final PaymentOrderQueryService paymentOrderQueryService;

    public void isExistProduct(List<ProductOutPut> productList) {

        if (Objects.isNull(productList) || productList.isEmpty()) {
            throw new BusinessException(PaymentErrorCode.NOT_EXIST_PRODUCT);
        }
    }

    public void paymentTotalAmountValidation(String orderId, String requestTotalAmount) {
        BigDecimal totalAmount = paymentOrderQueryService.selectTotalAmountByOrderId(orderId);

        if (totalAmount.compareTo(new BigDecimal(requestTotalAmount)) != 0) {
            throw new BusinessException(PaymentErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}
