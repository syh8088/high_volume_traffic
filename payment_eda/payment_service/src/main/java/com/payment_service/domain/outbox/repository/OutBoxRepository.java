package com.payment_service.domain.outbox.repository;

import com.payment_service.domain.outbox.model.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

}