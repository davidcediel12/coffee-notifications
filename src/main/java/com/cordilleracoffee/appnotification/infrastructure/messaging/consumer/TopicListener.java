package com.cordilleracoffee.appnotification.infrastructure.messaging.consumer;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicListener {

    private final KafkaReceiver<String, String> checkoutReceiver;

    public TopicListener(ReceiverOptions<String, String> receiverOptions) {

        receiverOptions.subscription(List.of("checkout"));
        receiverOptions.
        this.checkoutReceiver = KafkaReceiver.create();
    }
}
