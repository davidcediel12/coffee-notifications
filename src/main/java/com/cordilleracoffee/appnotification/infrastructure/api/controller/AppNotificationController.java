package com.cordilleracoffee.appnotification.infrastructure.api.controller;


import com.cordilleracoffee.appnotification.infrastructure.messaging.consumer.TopicListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/app-notification")
@RequiredArgsConstructor
@Slf4j
public class AppNotificationController {

    private final TopicListener topicListener;

    @PostMapping(value = "/register", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> suscribeToEvents(@RequestHeader("App-User-ID") String userId){
        return topicListener.getEvents()
                .doFirst(() -> log.info("User {} is subscribing to events", userId))
                .doOnNext(event -> log.info("New event received: {}", event))
                .map(event -> ServerSentEvent.<String>builder()
                        .data(event)
                        .event("New notification")
                        .build());
    }

    @PostMapping("/emit")
    public void emit(){
        topicListener.emit("Hello from App Notification");
        log.info("Event emitted");
    }
}
