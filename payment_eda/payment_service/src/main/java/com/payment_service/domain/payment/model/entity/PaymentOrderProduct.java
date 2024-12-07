package com.payment_service.domain.payment.model.entity;

import com.payment_service.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_order_product")
public class PaymentOrderProduct extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_order_no")
    private PaymentOrder paymentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private Product product;

    @Builder
    private PaymentOrderProduct(PaymentOrder paymentOrder, Product product) {
        this.paymentOrder = paymentOrder;
        this.product = product;
    }

    public static PaymentOrderProduct of(PaymentOrder paymentOrder, Product product) {
        return PaymentOrderProduct.builder()
                .paymentOrder(paymentOrder)
                .product(product)
                .build();
    }

    public static List<PaymentOrderProduct> of(PaymentOrder paymentOrder, List<Product> products) {
        return products.stream()
                .map(product -> {
                    return PaymentOrderProduct.of(paymentOrder, product);
                })
                .collect(Collectors.toList());
    }
}
