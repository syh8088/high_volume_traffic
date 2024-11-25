package com.webmain.domain.response;

import com.webmain.domain.entity.Notice;
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

    public static NoticeResponses ofList(List<Notice> noticeList) {
        return new NoticeResponses(NoticeResponse.getInstance(noticeList));
    }
}
