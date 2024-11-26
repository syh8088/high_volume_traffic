package com.accessor_queuing_system_webflux_main.controller;

import com.accessor_queuing_system_webflux_main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;


    @GetMapping("main")
    public String getMain(final Model model) {
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(mainService.selectNotices(), 1);
        model.addAttribute("noticeList", reactiveDataDrivenMode);

        return "main.html";
    }
}
