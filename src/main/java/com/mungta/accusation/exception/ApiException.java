package com.mungta.accusation.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ApiStatus apiStatus;

    public ApiException(ApiStatus apiStatus) {
        super(apiStatus.getMessage());
        this.apiStatus = apiStatus;
    }

    public ApiException(ApiStatus apiStatus, Throwable e) {
        super(apiStatus.getMessage(), e);
        this.apiStatus = apiStatus;
    }

}
