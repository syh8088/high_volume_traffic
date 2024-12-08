package com.payment_service.domain.outbox.service;

import com.payment_service.domain.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutBoxQueryService {

    private final OutBoxRepository outBoxRepository;
}
