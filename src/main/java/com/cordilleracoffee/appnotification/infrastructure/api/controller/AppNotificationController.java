package com.cordilleracoffee.appnotification.infrastructure.api.controller;


import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/app-notification")
public class AppNotificationController {


    @PostMapping(value = "/register", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> suscribeToEvents(@RequestHeader("App-User-ID") String userId){
        return null;
    }
}
