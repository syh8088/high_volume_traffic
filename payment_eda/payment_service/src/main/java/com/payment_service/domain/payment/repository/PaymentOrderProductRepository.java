package com.payment_service.domain.payment.repository;

import com.payment_service.domain.payment.model.entity.PaymentOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderProductRepository extends JpaRepository<PaymentOrderProduct, Long> {

}