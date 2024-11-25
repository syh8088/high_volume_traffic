package com.accessor_queuing_system_main.domain.response;

import lombok.Getter;

import java.util.List;

@Getter
public class NoticeResponses {

    private final List<NoticeResponse> noticeList;

    private NoticeResponses(List<NoticeResponse> noticeList) {
        this.noticeList = noticeList;
    }

    public static NoticeResponses of(List<NoticeResponse> noticeList) {
        return new NoticeResponses(noticeList);
    }
}
