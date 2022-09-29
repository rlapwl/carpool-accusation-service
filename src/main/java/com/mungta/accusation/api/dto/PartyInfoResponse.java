package com.mungta.accusation.api.dto;

import com.mungta.accusation.domain.PartyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@Builder
public class PartyInfoResponse {

    @Schema(description = "파티 ID")
    private long partyId;

    @Schema(description = "출발지")
    private String placeOfDeparture;

    @Schema(description = "도착지")
    private String destination;

    @Schema(description = "파티 시작했던 시간")
    private String startedDateTime;

    public static PartyInfoResponse of(PartyInfo partyInfo) {
        return PartyInfoResponse.builder()
                .partyId(partyInfo.getPartyId())
                .placeOfDeparture(partyInfo.getPlaceOfDeparture())
                .destination(partyInfo.getDestination())
                .startedDateTime(partyInfo.getStartedDateTime())
                .build();
    }

}
