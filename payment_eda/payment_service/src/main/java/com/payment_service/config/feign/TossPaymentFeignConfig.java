package com.payment_service.config.feign;

import com.payment_service.config.feign.handler.TossPaymentFeignErrorDecode;
import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class TossPaymentFeignConfig {

    @Value("${toss.secretKey}")
    private String secretKey;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public PaymentAuthenticationInterceptor basicAuthenticationInterceptor() {
        return new PaymentAuthenticationInterceptor(secretKey);
    }

    @Bean
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return new HttpMessageConverters(new RestTemplate().getMessageConverters());
            }
        }));
    }

    @Bean
    public TossPaymentFeignErrorDecode decoder() {
        return new TossPaymentFeignErrorDecode();
    }

}