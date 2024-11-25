package com.accessorqueuing.service;

import com.accessorqueuing.AccessorQueuingRedisRepository;
import com.accessorqueuing.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessorQueuingService {

    private final String USER_QUEUE_WAIT_KEY = "users:queue:%s:wait";
    private final String USER_QUEUE_WAIT_KEY_FOR_SCAN = "users:queue:*:wait";
    private final String USER_QUEUE_PROCEED_KEY = "users:queue:%s:proceed";

    private final AccessorQueuingRedisRepository accessorQueuingRedisRepository;

    @Value("${scheduler.enabled}")
    private Boolean scheduling = false;

    public Mono<Long> registerWaitQueue(final String queue, final Long userId) {
        long unixTimestamp = Instant.now().getEpochSecond();
        return accessorQueuingRedisRepository.zSetAdd(USER_QUEUE_WAIT_KEY, queue, userId, unixTimestamp)
                .filter(i -> i)
                .switchIfEmpty(Mono.error(ErrorCode.QUEUE_ALREADY_REGISTERED_USER.build()))
                .flatMap(i -> accessorQueuingRedisRepository.zRank(USER_QUEUE_WAIT_KEY, queue, userId))
                .map(i -> i >= 0 ? i + 1 : i);
    }

    public Mono<Long> allowUser(final String queue, final Long count) {
        return accessorQueuingRedisRepository.zPopMin(USER_QUEUE_WAIT_KEY, queue, count)
                .flatMap(user -> accessorQueuingRedisRepository.zSetAdd(
                        USER_QUEUE_PROCEED_KEY,
                        queue,
                        Long.valueOf(user.getValue()),
                        Instant.now().getEpochSecond()
                ))
                .count();
    }

    public Mono<Boolean> isAllowed(final String queue, final Long userId) {
        return accessorQueuingRedisRepository.zRank(USER_QUEUE_PROCEED_KEY, queue, userId)
                .defaultIfEmpty(-1L)
                .map(rank -> rank >= 0);
    }

    public Mono<Boolean> isAllowedByToken(final String queue, final Long userId, final String token) {
        return this.generateToken(queue, userId)
                .filter(gen -> gen.equalsIgnoreCase(token))
                .map(i -> true)
                .defaultIfEmpty(false);
    }

    public Mono<Long> getRank(final String queue, final Long userId) {
        return accessorQueuingRedisRepository.zRank(USER_QUEUE_WAIT_KEY, queue, userId)
                .defaultIfEmpty(-1L)
                .map(rank -> rank >= 0 ? rank + 1 : rank);
    }

    public Mono<String> generateToken(final String queue, final Long userId) {

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            String input = "user-queue-%s-%d".formatted(queue, userId);
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte aByte: encodedHash) {
                hexString.append(String.format("%02x", aByte));
            }

            return Mono.just(hexString.toString());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void scheduleAllowUser() {
        if (!scheduling) {
            log.info("passed scheduling...");
            return;
        }
        log.info("called scheduling...");

        var maxAllowUserCount = 100L;
        accessorQueuingRedisRepository.scan(USER_QUEUE_WAIT_KEY_FOR_SCAN, 100)
                .map(key -> key.split(":")[2])
                .flatMap(queue -> this.allowUser(queue, maxAllowUserCount).map(allowed -> Tuples.of(queue, allowed)))
                .doOnNext(tuple -> log.info("Tried %d and allowed %d members of %s queue".formatted(maxAllowUserCount, tuple.getT2(), tuple.getT1())))
                .subscribe();
    }

}










