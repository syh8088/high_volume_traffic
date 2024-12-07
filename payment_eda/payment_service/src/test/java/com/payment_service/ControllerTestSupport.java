package com.payment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.api.payment.controller.PaymentCheckoutController;
import com.payment_service.api.payment.controller.PaymentConfirmController;
import com.payment_service.api.payment.controller.PaymentController;
import com.payment_service.api.payment.service.PaymentApiService;
import com.payment_service.api.payment.service.PaymentCheckOutApiService;
import com.payment_service.api.payment.service.PaymentConfirmApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        PaymentCheckoutController.class,
        PaymentConfirmController.class,
        PaymentController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PaymentApiService paymentApiService;

    @MockBean
    protected PaymentConfirmApiService paymentConfirmApiService;

    @MockBean
    protected PaymentCheckOutApiService paymentCheckOutApiService;

}