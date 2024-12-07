package com.payment_service.domain.payment.repository;

import com.payment_service.domain.payment.model.entity.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long>, PaymentEventRepositoryCustom {

    @Modifying
    @Query("UPDATE PaymentEvent AS p SET p.paymentKey = :paymentKey, p.pspRawData = :pspRawData, p.approvedDateTime = :approvedDateTime, p.isPaymentDone = :isPaymentDone WHERE p.orderId = :orderId")
    void updatePaymentEventExtraDetails(
            @Param("orderId") String orderId,
            @Param("paymentKey") String paymentKey,
            @Param("pspRawData") String pspRawData,
            @Param("approvedDateTime") LocalDateTime approvedDateTime,
            @Param("isPaymentDone") boolean isPaymentDone
    );
}