package com.webmain.service;

import com.webmain.domain.entity.Notice;
import com.webmain.domain.response.NoticeResponse;
import com.webmain.repository.NoticeR2DbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final NoticeR2DbcRepository noticeR2DbcRepository;

    public Flux<NoticeResponse> selectNotices() {

        Flux<Notice> noticeFlux = noticeR2DbcRepository.findAll();
        return noticeFlux.flatMap(notice -> {
            NoticeResponse instance = NoticeResponse.getInstance(notice);
            return Flux.just(instance);
        });
    }
}
