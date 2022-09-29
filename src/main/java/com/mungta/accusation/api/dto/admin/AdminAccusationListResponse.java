package com.mungta.accusation.api.dto.admin;

import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class AdminAccusationListResponse {

    @Schema(description = "모든 회원 신고 리스트")
    private List<AdminAccusationInfoResponse> accusations;

    public static AdminAccusationListResponse of(List<Accusation> accusationList) {
        List<AdminAccusationInfoResponse> accusationResponseList = accusationList.stream()
                .map(AdminAccusationInfoResponse::of)
                .collect(Collectors.toList());

        return new AdminAccusationListResponse(accusationResponseList);
    }

    @EqualsAndHashCode
    @Getter
    @Builder
    public static class AdminAccusationInfoResponse {

        @Schema(description = "신고 ID")
        private long id;

        @Schema(description = "파티 ID")
        private long partyId;

        @Schema(description = "신고 등록한 회원 ID")
        private String memberId;

        @Schema(description = "제목")
        private String title;

        @Schema(description = "신고 상태")
        private AccusationStatus accusationStatus;

        @Schema(description = "최종 수정일")
        private String modifiedDateTime;

        public static AdminAccusationInfoResponse of(Accusation accusation) {
            return AdminAccusationInfoResponse.builder()
                    .id(accusation.getId())
                    .partyId(accusation.getPartyInfo().getPartyId())
                    .memberId(accusation.getMemberId())
                    .title(accusation.getAccusationContents().getTitle())
                    .accusationStatus(accusation.getAccusationStatus())
                    .modifiedDateTime(
                            accusation.getModifiedDateTime()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                    .build();
        }

    }

}
