package com.mungta.accusation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiStatus {
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -1, "예상치 못한 에러가 발생하였습니다."),
    INVALID_MODIFY_ACCUSATION(HttpStatus.BAD_REQUEST, -600, "이미 신고 처리된 상태라 내용을 수정할 수 없습니다."),
    INVALID_READ_ACCUSATION(HttpStatus.BAD_REQUEST, -601, "신고를 하지 않은 회원이므로 신고 내용을 볼 권한이 없습니다."),
    NOT_FOUND_ACCUSATION(HttpStatus.NOT_FOUND, -602, "해당 신고글을 찾을 수 없습니다."),
    INVALID_CHANGE_STATUS(HttpStatus.BAD_REQUEST, -603, "이미 관리자에 의해 처리된 신고글입니다."),
    SEND_EMAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -604, "메일 보내던 중 에러가 발생하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

}
