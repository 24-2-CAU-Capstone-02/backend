package com.capstone.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // AUTH
    SESSION_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED, "AUTH-001", "세션 토큰이 유효하지 않습니다."),
    MEMBER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-002", "권한이 없습니다."),

    // DB
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-001", "DB에서 Member를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-002", "이미 DB에 Member가 존재합니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-003", "DB에서 Menu를 찾을 수 없습니다."),
    MENU_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-004", "이미 DB에 Menu가 존재합니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-005", "DB에서 Room을 찾을 수 없습니다."),
    ROOM_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-006", "이미 DB에 Room이 존재합니다."),
    MENU_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-005", "DB에서 MenuImage를 찾을 수 없습니다."),

    // MAPPING
    MAPPING_ERROR(HttpStatus.BAD_REQUEST, "MAPPING-001", "Response 매핑에 실패했습니다."),

    DUMMY_ERROR_CODE(HttpStatus.OK, "DUMMY", "DUMMY_ERROR_CODE");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
