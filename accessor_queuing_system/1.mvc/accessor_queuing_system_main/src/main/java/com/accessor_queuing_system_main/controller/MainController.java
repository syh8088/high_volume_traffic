package com.accessor_queuing_system_main.controller;

import com.accessor_queuing_system_main.domain.response.NoticeResponse;
import com.accessor_queuing_system_main.domain.response.NoticeResponses;
import com.accessor_queuing_system_main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("main")
    public String getMain(Model model) {

        List<NoticeResponse> noticeResponseList = mainService.selectNotices();
        model.addAttribute("noticeList", noticeResponseList);

        return "main";
    }


}
