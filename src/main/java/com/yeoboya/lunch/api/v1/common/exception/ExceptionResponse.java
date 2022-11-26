package com.yeoboya.lunch.api.v1.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * <pre>
 * {
 *      "code": "400",
 *      "message": "잘못된 요청입니다.",
 *      "validation": {
 *          "title": "값을 입력해주세요"
 *      }
 * }
 * </pre>
 */

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private final int code;
    private final String message;
    private final LinkedList<LinkedHashMap<String, String>> validation;
    private final String note;

    @Builder
    public ExceptionResponse(int code, String message, LinkedList<LinkedHashMap<String, String>> validation, String note) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new LinkedList<>();
        this.note = note;
    }

}