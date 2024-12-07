package com.payment_service.api.payment.controller;

import org.springframework.stereotype.Component;

@Component
public class PartitionKeyUtil {

	// 파티션 키 카운트가 6인 이유는 카프카의 페이먼트 토픽 파티션 값이 6이라고 그렇다.
	private static final int PARTITION_KEY_COUNT = 1;

	public int createPartitionKey(int number) {
		return Math.abs(number) % PARTITION_KEY_COUNT;
	}
}