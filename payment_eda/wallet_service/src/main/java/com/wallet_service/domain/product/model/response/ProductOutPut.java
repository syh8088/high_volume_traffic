package com.wallet_service.domain.product.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.product.model.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class ProductOutPut {

    private long productNo;
    private long sellerNo;
    private String productId;
    private String name;
    private BigDecimal price;

    @QueryProjection
    public ProductOutPut(long productNo, long sellerNo, String productId, String name, BigDecimal price) {
        this.productNo = productNo;
        this.sellerNo = sellerNo;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public static String getOrderName(List<ProductOutPut> productList) {

        if (!Objects.isNull(productList) && !productList.isEmpty()) {
            return productList.get(0).getName() + " 그외";
        }
        else {
            return productList.get(0).getName();
        }
    }

    public static ProductOutPut of(Product product, long sellerNo) {
        return new ProductOutPut(product.getNo(), sellerNo, product.getProductId(), product.getName(), product.getPrice());
    }

    public static List<ProductOutPut> of(List<Product> productList, long sellerNo) {
        return productList.stream()
                .map(product -> ProductOutPut.of(product, sellerNo))
                .toList();
    }
}
