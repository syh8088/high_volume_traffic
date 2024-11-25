package com.accessor_queuing_system_main.service;

import com.accessor_queuing_system_main.domain.entity.Notice;
import com.accessor_queuing_system_main.domain.response.NoticeResponse;
import com.accessor_queuing_system_main.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final NoticeRepository noticeRepository;


    public List<NoticeResponse> selectNotices() {
        List<Notice> noticeList = noticeRepository.findAll();
        return NoticeResponse.getInstance(noticeList);
    }
}
