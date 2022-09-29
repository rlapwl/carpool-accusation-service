package com.mungta.accusation.api.dto.admin;

import com.mungta.accusation.api.dto.AccusationContentsResponse;
import com.mungta.accusation.api.dto.AccusedMemberResponse;
import com.mungta.accusation.api.dto.PartyInfoResponse;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminAccusationResponse {

    @Schema(description = "신고 ID")
    private long id;

    private AccusedMemberResponse accusedMember;

    private PartyInfoResponse partyInfo;

    private AccusationContentsResponse accusationContents;

    @Schema(description = "신고 상태")
    private AccusationStatus accusationStatus;

    @Schema(description = "관리자 코멘트")
    private String resultComment;

    public static AdminAccusationResponse of(Accusation accusation) {
        return AdminAccusationResponse.builder()
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
                .resultComment(accusation.getResultComment())
                .build();
    }

}
