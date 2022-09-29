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
public class AccusationContentsRequest {

    @Schema(description = "제목")
    @NotBlank(message = "{title.not.empty}")
    private String title;

    @Schema(description = "내용")
    private String desc;

}
