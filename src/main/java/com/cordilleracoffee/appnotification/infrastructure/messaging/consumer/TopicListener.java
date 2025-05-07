package com.cordilleracoffee.appnotification.infrastructure.messaging.consumer;


import com.cordilleracoffee.appnotification.infrastructure.messaging.consumer.dto.CheckoutEventType;
import com.cordilleracoffee.appnotification.infrastructure.messaging.consumer.dto.CheckoutMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class TopicListener {

    private final ObjectMapper objectMapper;
    private final Sinks.Many<String> eventSink;

    public TopicListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.eventSink = Sinks.many().multicast().directBestEffort();
    }

    @KafkaListener(groupId = "app-notifications", topics = "checkout")
    public void receiveCheckout(GenericMessage<String> message) {

        log.info("Received Message from checkout topic: {}", message.getPayload());
        CheckoutMessage checkoutMessage;

        try {
            checkoutMessage = objectMapper.readValue(message.getPayload(), CheckoutMessage.class);

            CheckoutEventType eventType = checkoutMessage.checkoutEventType();

            if (eventType == null) {
                return;
            }

            Sinks.EmitResult emitResult = eventSink.tryEmitNext(checkoutMessage.content().toString());

            if (emitResult.isSuccess()) {
                log.info("Event sink emitted: {}", checkoutMessage.content().asText());
            } else {
                log.error("Event sink emission failed");
            }
        } catch (JsonProcessingException e) {
            log.error("Unable to parse message", e);
            throw new RuntimeException(e);
        }
    }

    public void emit(String message) {
        eventSink.tryEmitNext(message);
    }

    public Flux<String> getEvents() {
        return eventSink.asFlux();
    }
}
