package com.mungta.accusation.messagequeue.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class AbstractEvent {

    protected String eventType;
    protected String timestamp;

    public AbstractEvent() {
        this.eventType = this.getClass().getSimpleName();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
