package com.wallet_service.domain.outbox.service;

import com.wallet_service.domain.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OutBoxCommandService {

    private final OutBoxRepository outBoxRepository;
}