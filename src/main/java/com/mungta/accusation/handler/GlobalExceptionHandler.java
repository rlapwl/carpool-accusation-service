package com.mungta.accusation.handler;

import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import com.mungta.accusation.exception.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<MessageEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[handleMethodArgumentNotValidException] ", e);
        return ResponseEntity.badRequest()
                .body(MessageEntity.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getFieldErrors().get(0).getDefaultMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected ResponseEntity<MessageEntity> handleMethodNotAllowedException(MethodNotAllowedException e) {
        log.error("[handleMethodArgumentNotValidException] ", e);
        return new ResponseEntity<>(
                MessageEntity.builder()
                        .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .message("해당 요청을 처리할 수 없습니다.")
                        .build(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<MessageEntity> handleApiException(ApiException e) {
        log.error("[ApiException] ", e);
        ApiStatus apiStatus = e.getApiStatus();
        return new ResponseEntity<>(MessageEntity.of(apiStatus), apiStatus.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<MessageEntity> handleRuntimeException(RuntimeException e) {
        log.error("[handleRuntimeException] ", e);
        return ResponseEntity.internalServerError()
                .body(MessageEntity.of(ApiStatus.UNEXPECTED_ERROR));
    }

}
