package com.payment_service.api.payment.service;

import com.payment_service.api.payment.model.request.PaymentCheckOutRequest;
import com.payment_service.api.payment.model.response.PaymentCheckOutResponse;
import com.payment_service.common.IdempotencyCreator;
import com.payment_service.domain.payment.model.response.PaymentCheckOutOutPut;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.payment.service.PaymentCheckOutQueryService;
import com.payment_service.domain.product.service.ProductQueryService;
import com.payment_service.domain.payment.validator.PaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCheckOutApiService {

    private final PaymentCheckOutQueryService paymentCheckOutQueryService;
    private final ProductQueryService productQueryService;
    private final PaymentValidator paymentValidator;

    @Transactional
    public PaymentCheckOutResponse paymentCheckOut(PaymentCheckOutRequest request) {

        List<ProductOutPut> productList
                = productQueryService.selectProductListByProductNoList(request.getProductNoList());

        paymentValidator.isExistProduct(productList);

        String idempotency = IdempotencyCreator.create(request);

        PaymentCheckOutOutPut paymentCheckOutOutPut = paymentCheckOutQueryService.paymentCheckOut(
                idempotency,
                productList
        );

        return PaymentCheckOutResponse.of(paymentCheckOutOutPut);
    }
}
