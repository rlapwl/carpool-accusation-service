package com.mungta.accusation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.mungta.accusation.constants.AccusationTestSample.*;
import static com.mungta.accusation.constants.AccusationTestSample.STARTED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

class PartyInfoTest {

    @DisplayName("equals test")
    @Test
    void testEquals_true() {
        PartyInfo partyInfo = getNewPartyInfo(PARTY_ID);
        PartyInfo other = getNewPartyInfo(PARTY_ID);

        assertThat(partyInfo.equals(other)).isEqualTo(true);
        assertThat(partyInfo.hashCode() == other.hashCode()).isEqualTo(true);
    }

    @DisplayName("not equals test")
    @Test
    void testEquals_false() {
        PartyInfo partyInfo = getNewPartyInfo(PARTY_ID);
        PartyInfo other = getNewPartyInfo(12);

        assertThat(partyInfo.equals(other)).isEqualTo(false);
        assertThat(partyInfo.hashCode() == other.hashCode()).isEqualTo(false);
    }

    private PartyInfo getNewPartyInfo(long id) {
        return PartyInfo.builder()
                .partyId(id)
                .placeOfDeparture(PLACE_OF_DEPARTURE)
                .destination(DESTINATION)
                .startedDateTime(STARTED_DATE_TIME)
                .build();
    }

}
