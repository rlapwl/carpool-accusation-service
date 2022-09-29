package com.mungta.accusation.api.dto;

import com.mungta.accusation.domain.AccusedMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@Builder
public class AccusedMemberResponse {

    @Schema(description = "신고 대상 회원 ID")
    private String id;

    @Schema(description = "신고 대상 회원 이름")
    private String name;

    public static AccusedMemberResponse of(AccusedMember accusedMember) {
        return AccusedMemberResponse.builder()
                .id(accusedMember.getId())
                .name(accusedMember.getName())
                .build();
    }
}
