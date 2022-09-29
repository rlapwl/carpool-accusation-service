package com.mungta.accusation.api.dto;

import com.mungta.accusation.domain.AccusationContents;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@Builder
public class AccusationContentsResponse {

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String desc;

    public static AccusationContentsResponse of(AccusationContents accusationContents) {
        return AccusationContentsResponse.builder()
                .title(accusationContents.getTitle())
                .desc(accusationContents.getDesc())
                .build();
    }

}
