package com.mungta.accusation.messagequeue.event;

import lombok.Getter;

@Getter
public class AccusationCompleted extends AbstractEvent {

    private final String accusedMemberId;
    private final long accusationId;

    public AccusationCompleted(String accusedMemberId, long accusationId) {
        super();
        this.accusedMemberId = accusedMemberId;
        this.accusationId = accusationId;
    }

    @Override
    public String toString() {
        return "AccusationCompleted{" +
                "eventType='" + eventType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", accusedMemberId='" + accusedMemberId + '\'' +
                ", accusationId=" + accusationId +
                '}';
    }

}
