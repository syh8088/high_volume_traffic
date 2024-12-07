package com.payment_service.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeConverter {

    /**
     * String 형식 날짜를 LocalDateTime 형식으로 변환
     *
     */
    public static LocalDateTime convertStringDateTimeToLocalDateTime(String stringDateTime) {
        if (!StringUtils.hasText(stringDateTime)) {
            return null;
        }

        return LocalDateTime.parse(stringDateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
