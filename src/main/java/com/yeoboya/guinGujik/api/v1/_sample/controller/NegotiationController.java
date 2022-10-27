package com.yeoboya.guinGujik.api.v1._sample.controller;


import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Media Type Versioning 을 이용한 버저닝
 * </pre>
 *
 * @author 김현진
 * @see <a href="https://www.iana.org/assignments/media-types/application/vnd.api+json/">vnd</a> <p>
 */


@Slf4j
@RestController
@RequestMapping("/v1/nego")
public class NegotiationController {

    private final Response response;

    public NegotiationController(Response response) {
        this.response = response;
    }


//    @GetMapping(value= "/{id}")
//    public ResponseEntity<?> getPostByIdWeb(@PathVariable(name= "id") long id){
//        log.info("{}", "application/pc");
//        return response.success("성공");
//    }
//
    @GetMapping(value= "/{id}", produces= "application/vnd.yeoboya.web+json")
    public ResponseEntity<?> getPostByIdWeb(@PathVariable(name= "id") long id){
        log.info("{}", "application/vnd.yeoboya.web+json");
        return response.success("성공");
    }


    @GetMapping(value= "/{id}", produces="application/vnd.yeoboya.ios+json")
    public ResponseEntity<?> getPostByIdIos(@PathVariable(name= "id") long id){
        log.info("{}", "application/vnd.yeoboya.ios+json");
        return response.success("성공");
    }

    @GetMapping(value= "/{id}", produces="application/vnd.yeoboya.android+json")
    public ResponseEntity<?> getPostByIdAndroid(@PathVariable(name= "id") long id){
        log.info("{}", "application/vnd.yeoboya.android+json");
        return response.success("성공");
    }

}
