package com.webmain.repository;

import com.webmain.domain.entity.Notice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface NoticeR2DbcRepository extends ReactiveCrudRepository<Notice, Long>, NoticeCustomR2dbcRepository {
    Flux<Notice> findByNo(Long no);
}
