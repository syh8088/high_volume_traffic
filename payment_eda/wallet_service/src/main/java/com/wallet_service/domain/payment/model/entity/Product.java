package com.wallet_service.domain.payment.model.entity;

import com.wallet_service.common.entity.CommonEntity;
import com.wallet_service.domain.seller.model.entity.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_no")
    private Seller seller;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Builder
    private Product(Long no, Seller seller, String productId, String name, BigDecimal price) {
        this.no = no;
        this.seller = seller;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }
}
