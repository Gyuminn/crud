package com.crud.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "code": "400",
 * "message": "잘못된 요청입니다.",
 * "validation": {
 * "title": "값을 입력해주세요",
 * "content":
 * }
 * }
 */

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    // Map을 쓰는 것보다 좋은 방법이 있는지 개선해보기
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}
