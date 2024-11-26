package com.accessorqueuing.controller;

import com.accessorqueuing.common.IdempotencyCreator;
import com.accessorqueuing.service.AccessorQueuingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class WaitingRoomController {
    private final AccessorQueuingService accessorQueuingService;

    @GetMapping("test")
    public Mono<Rendering> waitingRoomPage() {
       return Mono.just(Rendering.view("waiting-room.html").build());
    }

    @GetMapping("/waiting-room")
    Mono<Rendering> getWaitingRoom(
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey", required = false) String idempotencyKey,
            @RequestParam(name = "redirect_url", required = false) String redirectUrl,
            ServerWebExchange exchange,
            ServerHttpRequest request
    ) {

        if (Objects.isNull(idempotencyKey)) {
            idempotencyKey = IdempotencyCreator.create(request);
            redirectUrl = "http://localhost:8080/main?idempotencyKey=%s".formatted(idempotencyKey);
        }

        var key = "user-queue-%s-token".formatted(queue);
        var cookieValue = exchange.getRequest().getCookies().getFirst(key);
        var token = (cookieValue == null) ? "" : cookieValue.getValue();

        String finalRedirectUrl = redirectUrl;
        String finalIdempotencyKey = idempotencyKey;

        return accessorQueuingService.isAllowedByToken(queue, idempotencyKey, token)
                .filter(allowed -> allowed)
                .flatMap(allowed -> Mono.just(Rendering.redirectTo(finalRedirectUrl).build()))
                .switchIfEmpty(
                        accessorQueuingService.registerWaitQueue(queue, idempotencyKey)
                                .onErrorResume(ex -> accessorQueuingService.getRank(queue, finalIdempotencyKey))
                                .map(rank -> Rendering.view("waiting-room.html")
                                        .modelAttribute("number", rank)
                                        .modelAttribute("idempotencyKey", finalIdempotencyKey)
                                        .modelAttribute("queue", queue)
                                        .build()
                                )
                );
    }
}
