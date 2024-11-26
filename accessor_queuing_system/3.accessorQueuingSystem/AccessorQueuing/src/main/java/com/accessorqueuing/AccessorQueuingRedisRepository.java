package com.accessorqueuing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveZSetOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccessorQueuingRedisRepository {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final String USER_QUEUE_WAIT_KEY = "users:queue:%s:wait";
    private final String USER_QUEUE_WAIT_KEY_FOR_SCAN = "users:queue:*:wait";
    private final String USER_QUEUE_PROCEED_KEY = "users:queue:%s:proceed";

    public ReactiveZSetOperations<String, String> redisTemplateOpsForZset() {
        return reactiveRedisTemplate.opsForZSet();
    }

    public Mono<Boolean> zSetAdd(String key, String queue, String idempotencyKey, long unixTimestamp) {
        return this.redisTemplateOpsForZset().add(key.formatted(queue), idempotencyKey, unixTimestamp);
    }

    public Mono<Long> zRank(String key, String queue, String idempotencyKey) {
        return this.redisTemplateOpsForZset().rank(key.formatted(queue), idempotencyKey);
    }

    public Flux<ZSetOperations.TypedTuple<String>> zPopMin(String key, String queue, Long count) {
        return this.redisTemplateOpsForZset().popMin(key.formatted(queue), count);
    }

    public Flux<String> scan(String key, int count) {
        return reactiveRedisTemplate.scan(ScanOptions.scanOptions()
                .match(key)
                .count(count)
                .build());
    }
}
