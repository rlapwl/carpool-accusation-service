package com.mungta.accusation.messagequeue.event;

public enum PenaltyEventType {
    SUCCEED("PenaltySucceed"),
    FAILED("PenaltyFailed");

    private final String eventType;

    PenaltyEventType(String eventType) {
        this.eventType = eventType;
    }

    public static boolean isSucceed(String eventType) {
        return SUCCEED.eventType.equals(eventType);
    }

}
