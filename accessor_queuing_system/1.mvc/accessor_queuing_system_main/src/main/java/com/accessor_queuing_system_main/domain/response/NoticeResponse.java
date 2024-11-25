package com.accessor_queuing_system_main.domain.response;

import com.accessor_queuing_system_main.domain.entity.Notice;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NoticeResponse {

    private long no;
    private String title;
    private String content;

    private NoticeResponse(long no, String title, String content) {
        this.no = no;
        this.title = title;
        this.content = content;
    }

    public static NoticeResponse getInstance(long no, String title, String content) {
        return new NoticeResponse(no, title, content);
    }

    public static NoticeResponse getInstance(Notice notice) {
        return new NoticeResponse(notice.getNo(), notice.getTitle(), notice.getContent());
    }

    public static List<NoticeResponse> getInstance(List<Notice> noticeList) {
        return noticeList.stream()
                .map(NoticeResponse::getInstance)
                .collect(Collectors.toList());
    }
}
