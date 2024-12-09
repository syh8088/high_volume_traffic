package com.payment_service.domain.outbox.repository;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.model.response.OutBoxOutPut;

import java.util.List;

public interface OutBoxRepositoryCustom {

    List<OutBoxOutPut> selectOutBoxPendingEventMessageList(OutBoxStatus outBoxStatus);
}
