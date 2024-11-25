package com.accessor_queuing_system_webflux_main.service;

import com.accessor_queuing_system_webflux_main.domain.entity.Notice;
import com.accessor_queuing_system_webflux_main.repository.NoticeR2DbcRepository;
import com.accessor_queuing_system_webflux_main.domain.response.NoticeResponse;
import com.accessor_queuing_system_webflux_main.domain.response.NoticeResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final NoticeR2DbcRepository noticeR2DbcRepository;

    public Mono<NoticeResponses> selectNotices() {

        Flux<Notice> noticeFlux = noticeR2DbcRepository.findAll();
        return noticeFlux.map(data -> {
                    return NoticeResponse.getInstance(data);
                }).collectList()
                .map(data -> {
                     return NoticeResponses.of(data);
                });
    }
}
