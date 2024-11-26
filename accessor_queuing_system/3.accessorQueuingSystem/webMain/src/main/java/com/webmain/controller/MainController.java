package com.webmain.controller;

import com.webmain.common.IdempotencyCreator;
import com.webmain.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    RestTemplate restTemplate = new RestTemplate();

    private final MainService mainService;

    @GetMapping("main")
    public String main(
            final Model model,
            @RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "idempotencyKey", required = false) String idempotencyKey,
            ServerHttpRequest request
    ) {

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        String cookieName = "user-queue-%s-token".formatted(queue);

        String token = null;
        HttpCookie cookie = null;
        if (cookies != null) {
            for (String key : cookies.keySet()) {
                List<HttpCookie> httpCookies = cookies.get(key);
                for (HttpCookie httpCookie : httpCookies) {
                    if (httpCookie.getName().equals(cookieName)) {
                        cookie = httpCookie;
                        break;
                    }
                }
            }

            token = (Objects.isNull(cookie)) ? new HttpCookie(cookieName, "").getValue() : cookie.getValue();
        }

        idempotencyKey = (Objects.isNull(idempotencyKey)) ? IdempotencyCreator.create(request): idempotencyKey;
        var uri = UriComponentsBuilder
                .fromUriString("http://localhost:8081")
                .path("/api/accessor-queuing/allowed-user")
                .queryParam("queue", queue)
                .queryParam("idempotencyKey", idempotencyKey)
                .queryParam("token", token)
                .encode()
                .build()
                .toUri();

        ResponseEntity<AllowedUserResponse> response
                = restTemplate.getForEntity(uri, AllowedUserResponse.class);

        response.getBody();
        if (!response.getBody().allowed()) {

            // 대기 웹페이지로 리다이렉트
            return "redirect:http://localhost:8081/waiting-room?idempotencyKey=%s&redirect_url=%s".formatted(
                    idempotencyKey, "http://localhost:8080/main?idempotencyKey=%s".formatted(idempotencyKey));
        }

        // 허용 상태라면 해당 페이지를 진입
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(mainService.selectNotices(), 1);
        model.addAttribute("noticeList", reactiveDataDrivenMode);

        return "main.html";
    }

    public record AllowedUserResponse(Boolean allowed) {
    }
}
