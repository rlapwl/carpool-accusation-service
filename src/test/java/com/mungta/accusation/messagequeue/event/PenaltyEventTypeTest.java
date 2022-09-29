package com.mungta.accusation.messagequeue.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PenaltyEventTypeTest {

    @DisplayName("EventType이 PenaltySucceed인 경우 true 반환.")
    @Test
    void isSucceed_true() {
        boolean result = PenaltyEventType.isSucceed("PenaltySucceed");
        assertThat(result).isTrue();
    }

    @DisplayName("EventType이 PenaltyFailed인 경우 true 반환.")
    @Test
    void isSucceed_false() {
        boolean result = PenaltyEventType.isSucceed("PenaltyFailed");
        assertThat(result).isFalse();
    }

}
