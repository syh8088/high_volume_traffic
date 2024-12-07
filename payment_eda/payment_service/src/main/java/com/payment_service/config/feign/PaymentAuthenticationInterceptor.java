package com.payment_service.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Base64;

public class PaymentAuthenticationInterceptor implements RequestInterceptor {

    private final String encodedSecretKey;

    public PaymentAuthenticationInterceptor(final String secretKey) {

        this.encodedSecretKey = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", this.encodedSecretKey);
    }
}