package com.payment_service.domain.product.repository;

import com.payment_service.IntegrationTestSupport;
import com.payment_service.domain.payment.model.entity.Product;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("등록된 상품들을 조회한다.")
    void selectProduct() {
        // given
        Product product1 = this.createProduct("AAA", "상품A", BigDecimal.valueOf(1000));
        Product product2 = this.createProduct("BBB", "상품B", BigDecimal.valueOf(2000));
        Product product3 = this.createProduct("CCC", "상품C", BigDecimal.valueOf(3000));

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.selectProductAll();

        // then
        assertThat(products).hasSize(3)
                .extracting("productId", "name", "price")
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class).build()
                )
                .containsExactlyInAnyOrder(
                        tuple("AAA", "상품A", BigDecimal.valueOf(1000)),
                        tuple("BBB", "상품B", BigDecimal.valueOf(2000)),
                        tuple("CCC", "상품C", BigDecimal.valueOf(3000))
                );
    }

    private Product createProduct(String productId, String name, BigDecimal price) {

        return Product.of(productId, name, price);
    }

}