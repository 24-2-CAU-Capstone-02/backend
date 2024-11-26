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
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, "AUTH-003", "비밀번호가 일치하지 않습니다."),
    GOOGLE_FETCH_ACCESS_TOKEN_FAIL(HttpStatus.UNAUTHORIZED, "AUTH-004", "구글에서 Access Token을 받아오는 데에 실패하였습니다."),
    GOOGLE_FETCH_USER_DATA_FAIL(HttpStatus.UNAUTHORIZED, "AUTH-005", "구글에서 사용자 정보를 받아오는 데에 실패하였습니다."),

    // DB
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-001", "DB에서 Member를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-002", "이미 DB에 Member가 존재합니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-003", "DB에서 Menu를 찾을 수 없습니다."),
    MENU_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-004", "이미 DB에 Menu가 존재합니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-005", "DB에서 Room을 찾을 수 없습니다."),
    ROOM_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "DB-006", "이미 DB에 Room이 존재합니다."),
    MENU_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-007", "DB에서 MenuImage를 찾을 수 없습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "DB-009", "DB에서 Account를 찾을 수 없습니다."),

    // MAPPING
    MAPPING_ERROR(HttpStatus.BAD_REQUEST, "MAPPING-001", "Response 매핑에 실패했습니다."),
    JSON_MAPPING_ERROR(HttpStatus.BAD_REQUEST, "MAPPING-002", "Open ai response 매핑에 실패했습니다."),

    // S3
    AWS_S3_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AWS-001", "AWS S3에 접근할 수 있는 권한이 없거나 인증에 실패했습니다."),
    AWS_S3_NOT_CONNECTED(HttpStatus.SERVICE_UNAVAILABLE, "AWS-002", "AWS S3 연결에 실패했습니다."),
    FAILED_TO_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "AWS-003", "파일 읽기 오류 혹은 잘못된 입력입니다."),

    // IMAGE
    IMAGE_COMPRESS_FAILED(HttpStatus.BAD_REQUEST, "IMAGE-001", "이미지 압축에 실패했습니다."),

    // REQUEST
    EXCEEDED_DAILY_ROOM_LIMIT(HttpStatus.BAD_REQUEST, "REQUEST-001", "하루에 만들 수 있는 방의 개수를 초과했습니다."),
    MENU_IMAGE_TOO_MANY(HttpStatus.TOO_MANY_REQUESTS, "REQUEST-002", "Room에 등록되는 이미지 개수가 제한을 초과합니다."),

    DUMMY_ERROR_CODE(HttpStatus.OK, "DUMMY", "DUMMY_ERROR_CODE");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
