package com.yeoboya.lunch.api.v1.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class Response {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Body {
        private final int code;
        private final String message;
        private String detail;
        private Object data;
    }

    /** 성공 */
    public ResponseEntity<Body> success(HttpStatus status, String msg, Object data) {
        Body body = Body.builder()
                .code(status.value())
                .message(msg)
                .data(data)
                .build();
        return ResponseEntity.status(status.value()).body(body);
    }

    /** 실패 */
    public ResponseEntity<Body> fail(ErrorCode errorCode) {
        Body body = Body.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMsg())
                .build();
        return  ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    /** 실패 */
    public ResponseEntity<Body> fail(ErrorCode errorCode, String detail) {
        Body body = Body.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMsg())
                .detail(detail)
                .build();
        return  ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    /**
     * <p> 성공 응답만 반환한다. </p>
     * <pre>
     *     {
     *         "statusCode" : 200,
     *         "result" : success
     *     }
     * </pre>
     *
     * @return 응답 객체
     */
    public ResponseEntity<Body> success() {
        return success(HttpStatus.OK, null, Collections.emptyList());
    }

    /**
     * <p> 데이터, 메세지를 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "statusCode" : code.getHttpStatus,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...]
     *     }
     * </pre>
     *
     * @param code Code.java
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(Code code) {
        return success(code.getHttpStatus(), code.getMsg(), null);
    }


    /**
     * <p> 메세지만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "statusCode" : 200,
     *         "result" : success,
     *         "message" : message
     *     }
     * </pre>
     *
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(String msg) {
        return success(HttpStatus.OK, msg, Collections.emptyList());
    }

    /**
     * <p> 데이터만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "statusCode" : 200,
     *         "result" : success,
     *         "data" : [{data1}, {data2}...]
     *     }
     * </pre>
     *
     * @param data 응답 바디 data 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(Object data) {
        return success(HttpStatus.OK, null, data);
    }


    /**
     * <p> 데이터, 메세지를 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "statusCode" : 200,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...]
     *     }
     * </pre>
     *
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @param data 응답 바디 data 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(String msg, Object data) {
        return success(HttpStatus.OK, msg, data);
    }

    /**
     * <p> 데이터, 메세지를 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "statusCode" : code.getHttpStatus(),
     *         "result" : success,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...]
     *     }
     * </pre>
     *
     * @param data 응답 바디 data 필드에 포함될 정보
     * @param code Code.java
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(Code code, Object data) {
        return success(code.getHttpStatus(), code.getMsg(), data);
    }

}
