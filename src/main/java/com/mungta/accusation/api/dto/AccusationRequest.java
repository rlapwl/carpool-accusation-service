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
public class AccusationRequest {

    private PartyInfoRequest partyInfo;

    private AccusedMemberRequest accusedMember;

    private AccusationContentsRequest accusationContents;

}
