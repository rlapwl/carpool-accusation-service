package com.mungta.accusation.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PartyInfo {

    @Column(name = "party_id", nullable = false)
    private Long partyId;

    @Column(name = "place_of_departure", nullable = false)
    private String placeOfDeparture;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "party_started_date_time", nullable = false)
    private String startedDateTime;

    @Builder
    public PartyInfo(Long partyId, String placeOfDeparture, String destination, String startedDateTime) {
        this.partyId = partyId;
        this.placeOfDeparture = placeOfDeparture;
        this.destination = destination;
        this.startedDateTime = startedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartyInfo partyInfo = (PartyInfo) o;
        return Objects.equals(partyId, partyInfo.partyId)
                && Objects.equals(placeOfDeparture, partyInfo.placeOfDeparture)
                && Objects.equals(destination, partyInfo.destination)
                && Objects.equals(startedDateTime, partyInfo.startedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, placeOfDeparture, destination, startedDateTime);
    }

}
