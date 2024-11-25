package com.webmain.repository;

import com.webmain.domain.entity.Notice;
import reactor.core.publisher.Flux;

public interface NoticeCustomR2dbcRepository {
    Flux<Notice> findAll();
}

