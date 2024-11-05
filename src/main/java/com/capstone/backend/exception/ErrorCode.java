package com.capstone.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUMMY_ERROR_CODE(HttpStatus.OK, "DUMMY", "DUMMY_ERROR_CODE");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
