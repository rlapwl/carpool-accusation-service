package com.mungta.accusation.api.dto.admin;

import com.mungta.accusation.domain.AccusationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccusationStatusRequest {

    @Schema(description = "신고 상태")
    private AccusationStatus accusationStatus;

    @Schema(description = "관리자 코멘트")
    private String resultComment;

}
