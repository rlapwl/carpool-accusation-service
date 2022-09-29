package com.mungta.accusation.client.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PartyResponse {

    private long partyId;
    private String placeOfDeparture;
    private String destination;
    private String startDate;
    private List<String> userIds;

}
