package com.mungta.accusation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccusedMemberRequest {

    @Schema(description = "신고 대상 회원 ID")
    @NotBlank(message = "{id.not.empty}")
    private String id;

    @Schema(description = "신고 대상 회원 이름")
    @NotBlank(message = "{name.not.empty}")
    private String name;

    @Schema(description = "신고 대상 회원 Email")
    @Email(message = "{email.not.valid}")
    private String email;

}
