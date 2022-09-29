package com.mungta.accusation.api.dto;

import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccusationResponse {

    @Schema(description = "신고 ID")
    private long id;

    private AccusedMemberResponse accusedMember;

    private PartyInfoResponse partyInfo;

    private AccusationContentsResponse accusationContents;

    @Schema(description = "신고 상태")
    private AccusationStatus accusationStatus;

    public static AccusationResponse of(Accusation accusation) {
        return AccusationResponse.builder()
                .id(accusation.getId())
                .accusedMember(
                        AccusedMemberResponse.of(accusation.getAccusedMember())
                )
                .partyInfo(
                        PartyInfoResponse.of(accusation.getPartyInfo())
                )
                .accusationContents(
                        AccusationContentsResponse.of(accusation.getAccusationContents())
                )
                .accusationStatus(accusation.getAccusationStatus())
                .build();
    }

}
