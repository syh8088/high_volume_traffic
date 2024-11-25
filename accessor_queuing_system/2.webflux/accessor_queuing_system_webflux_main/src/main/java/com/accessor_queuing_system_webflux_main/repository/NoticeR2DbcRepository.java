package com.accessor_queuing_system_webflux_main.repository;

import com.accessor_queuing_system_webflux_main.domain.entity.Notice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface NoticeR2DbcRepository extends ReactiveCrudRepository<Notice, Long>, NoticeCustomR2dbcRepository {
    Flux<Notice> findByNo(Long no);
}
