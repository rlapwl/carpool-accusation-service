package com.mungta.accusation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyInfoRequest {

    @Schema(description = "파티 ID")
    private long partyId;

    @Schema(description = "출발지")
    @NotBlank(message = "{placeOfDeparture.not.empty}")
    private String placeOfDeparture;

    @Schema(description = "도착지")
    @NotBlank(message = "{destination.not.empty}")
    private String destination;

    @Schema(description = "파티 시작했던 시간")
    @NotBlank(message = "{startedDateTime.not.empty}")
    private String startedDateTime;

}
