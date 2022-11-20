package com.yeoboya.lunch.api.v1.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class Response {

    /** 성공 */
    public ResponseEntity<Body> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .code(status.value())
                .message(msg)
                .data(data)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /** 실패 */
    public ResponseEntity<Body> fail(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .code(status.value())
                .message(msg)
                .data(data)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<Body> invalidFields(LinkedList<LinkedHashMap<String, String>> errors) {
        Body body = Body.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .error(errors)
                .build();
        return ResponseEntity.ok(body);
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
        return success(Collections.emptyList(), null, HttpStatus.OK);
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
        return success(data, null, HttpStatus.OK);
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
        return success(Collections.emptyList(), msg, HttpStatus.OK);
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
     * @param data 응답 바디 data 필드에 포함될 정보
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<Body> success(Object data, String msg) {
        return success(data, msg, HttpStatus.OK);
    }

    /**
     * <p> 메세지를 가진 실패 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "statusCode" : HttpStatus Code,
     *         "result" : fail,
     *         "message" : message,
     *         "error" : [{error1}, {error2}...]
     *     }
     * </pre>
     *
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @param status 응답 바디 status 필드에 포함될 응답 상태 코드
     * @return 응답 객체
     */
    public ResponseEntity<Body> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }

}