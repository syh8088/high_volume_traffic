package com.payment_service.domain.outbox.service;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.model.response.OutBoxOutPut;
import com.payment_service.domain.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutBoxQueryService {

    private final OutBoxRepository outBoxRepository;

    public List<OutBoxOutPut> selectOutBoxPendingEventMessageList() {
        return outBoxRepository.selectOutBoxPendingEventMessageList(OutBoxStatus.INIT);
    }
}
