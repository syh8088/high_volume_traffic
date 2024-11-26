package com.accessorqueuing.controller;

import com.accessorqueuing.dto.AllowUserResponse;
import com.accessorqueuing.dto.AllowedUserResponse;
import com.accessorqueuing.dto.RankNumberResponse;
import com.accessorqueuing.dto.RegisterUserResponse;
import com.accessorqueuing.service.AccessorQueuingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/accessor-queuing")
@RequiredArgsConstructor
public class AccessorQueuingController {

    private final AccessorQueuingService accessorQueuingService;

    @PostMapping("user")
    public Mono<RegisterUserResponse> registerUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey") String idempotencyKey
    ) {
        return accessorQueuingService.registerWaitQueue(queue, idempotencyKey)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("/allow")
    public Mono<AllowUserResponse> allowUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "count") Long count
    ) {
        return accessorQueuingService.allowUser(queue, count)
                .map(allowed -> new AllowUserResponse(count, allowed));
    }

    @GetMapping("/allowed-user")
    public Mono<AllowedUserResponse> isAllowedUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey") String idempotencyKey,
            @RequestParam(name = "token") String token
    ) {
        return accessorQueuingService.isAllowedByToken(queue, idempotencyKey, token)
                .map(AllowedUserResponse::new);
    }

    @GetMapping("/rank")
    public Mono<RankNumberResponse> getRankUser(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey") String idempotencyKey
    ) {
        return accessorQueuingService.getRank(queue, idempotencyKey)
                .map(RankNumberResponse::new);
    }

    @GetMapping("/touch")
    Mono<?> touch(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey") String idempotencyKey,
            ServerWebExchange exchange
    ) {
        return Mono.defer(() -> accessorQueuingService.generateToken(queue, idempotencyKey))
                .map(token -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie
                                    .from("user-queue-%s-token".formatted(queue), token)
                                    .maxAge(Duration.ofSeconds(300))
                                    .path("/")
                                    .build()
                    );

                    return token;
                });
    }


}
