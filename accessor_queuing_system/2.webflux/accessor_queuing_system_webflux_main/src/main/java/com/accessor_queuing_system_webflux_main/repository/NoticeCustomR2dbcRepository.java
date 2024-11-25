package com.accessor_queuing_system_webflux_main.repository;

import com.accessor_queuing_system_webflux_main.domain.entity.Notice;
import reactor.core.publisher.Flux;

public interface NoticeCustomR2dbcRepository {
    Flux<Notice> findAll();
}

