package com.payment_service.domain.payment.repository;

import com.payment_service.domain.payment.model.entity.PaymentOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderHistoryRepository extends JpaRepository<PaymentOrderHistory, Long> {

}