package com.mungta.accusation.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageEntity {
    private final int code;
    private final String message;

    private MessageEntity(ApiStatus apiStatus) {
        this.code = apiStatus.getCode();
        this.message = apiStatus.getMessage();
    }

    public static MessageEntity of(ApiStatus apiStatus) {
        return new MessageEntity(apiStatus);
    }

}
