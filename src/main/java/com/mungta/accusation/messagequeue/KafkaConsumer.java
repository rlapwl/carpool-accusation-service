package com.mungta.accusation.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import com.mungta.accusation.messagequeue.event.PenaltyEvent;
import com.mungta.accusation.service.AdminAccusationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

    private final AdminAccusationService adminAccusationService;

    @KafkaListener(topics = "user-topic", groupId ="com.example")
    public void sendEmailWhenPenaltySucceed(@Payload String message) {
        log.debug("Kafka User Consumed message = " + message);

        PenaltyEvent penaltyEvent = convertToPenaltyEvent(message);

        if (penaltyEvent.isSucceed()) {
            log.info("PenaltySucceed Message received from UserService.");
            adminAccusationService.sendPenaltyEmail(Long.parseLong(penaltyEvent.getAccusationId()));
        } else {
            log.info("PenaltyFailed Message received from UserService.");
            adminAccusationService.resetComment(Long.parseLong(penaltyEvent.getAccusationId()));
        }
    }

    private PenaltyEvent convertToPenaltyEvent(String message) {
        try {
            return new ObjectMapper().readValue(message, PenaltyEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Error json parse.", e);
            throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
        }
    }

}
