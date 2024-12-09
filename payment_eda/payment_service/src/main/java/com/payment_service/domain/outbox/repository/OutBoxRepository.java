package com.payment_service.domain.outbox.repository;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.model.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, Long>, OutBoxRepositoryCustom {

    @Modifying
    @Query("UPDATE OutBox AS o SET o.status = :outBoxStatus WHERE o.idempotencyKey = :idempotencyKey")
    void updateStatusByIdempotencyKey(@Param("idempotencyKey") String idempotencyKey, @Param("outBoxStatus") OutBoxStatus outBoxStatus);
}