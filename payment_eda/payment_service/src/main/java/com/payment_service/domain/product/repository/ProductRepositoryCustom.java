package com.payment_service.domain.product.repository;


import com.payment_service.domain.product.model.response.ProductOutPut;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductOutPut> selectProductListByProductNoList(List<Long> productNoList);
}
