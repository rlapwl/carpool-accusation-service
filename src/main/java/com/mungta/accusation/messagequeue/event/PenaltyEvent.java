package com.mungta.accusation.messagequeue.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PenaltyEvent extends AbstractEvent {

    private String accusedMemberId;

    private String accusationId;

    public boolean isSucceed() {
        return PenaltyEventType.isSucceed(eventType);
    }

    @Override
    public String toString() {
        return "PenaltyEvent{" +
                "eventType='" + eventType + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", accusedMemberId='" + accusedMemberId + '\'' +
                ", accusationId=" + accusationId +
                '}';
    }

}
