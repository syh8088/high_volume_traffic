package com.payment_service.domain.product.service;

import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    public List<ProductOutPut> selectProductListByProductNoList(List<Long> productNoList) {
        return productRepository.selectProductListByProductNoList(productNoList);
    }
}
