package com.mungta.accusation.messagequeue;

import com.mungta.accusation.messagequeue.event.AbstractEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final StreamBridge streamBridge;

    public void send(String bindingName, AbstractEvent event) {
        log.info("Kafka Producer send data: " + event);

        streamBridge.send(bindingName, MessageBuilder
                .withPayload(event)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build()
        );
    }

}
