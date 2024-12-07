package com.payment_service.api.payment.controller;

import com.payment_service.api.payment.model.request.PaymentConfirmRequest;
import com.payment_service.api.payment.service.PaymentConfirmApiService;
import com.payment_service.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentConfirmController {

    private final PaymentConfirmApiService paymentConfirmApiService;

    @PostMapping("confirm")
    public ApiResponse<?> paymentConfirm(@RequestBody PaymentConfirmRequest request) {

        paymentConfirmApiService.paymentConfirm(request);
        return ApiResponse.noContent();
    }

}
