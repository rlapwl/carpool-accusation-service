package com.mungta.accusation.api.dto;

import com.mungta.accusation.client.dto.PartyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccusationPartyMemberListResponse {

    @Schema(description = "파티 ID")
    private long partyId;

    @Schema(description = "출발지")
    private String placeOfDeparture;

    @Schema(description = "도착지")
    private String destination;

    @Schema(description = "파티 시작했던 시간")
    private String startedDateTime;

    @Schema(description = "파티원들")
    private List<MemberResponse> members;

    public static AccusationPartyMemberListResponse of(PartyResponse party, List<MemberResponse> members) {
        return AccusationPartyMemberListResponse.builder()
                .partyId(party.getPartyId())
                .placeOfDeparture(party.getPlaceOfDeparture())
                .destination(party.getDestination())
                .startedDateTime(party.getStartDate())
                .members(members)
                .build();
    }

    @EqualsAndHashCode
    @Getter
    @Builder
    public static class MemberResponse {

        @Schema(description = "회원 ID")
        private String id;

        @Schema(description = "회원 이름")
        private String name;

        @Schema(description = "회원 Email")
        private String email;

        @Schema(description = "회원 부서")
        private String department;

        @Schema(description = "회원 사진")
        private byte[] userPhoto;

        @Schema(description = "회원 사진 확장자")
        private String fileExtension;

        @Schema(description = "전에 신고했었는지 여부")
        private boolean accusedYN;
    }

}
