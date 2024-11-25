package com.accessor_queuing_system_webflux_main.controller;

import com.accessor_queuing_system_webflux_main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;


    @GetMapping("main")
    Mono<Rendering> getMain() {
        return mainService.selectNotices()
                .map(notice -> {

                    return Rendering.view("main.html")
                                    .modelAttribute("noticeList", notice.getNoticeList())
                                    .build();
                        }
                );
    }
}
