package com.yeoboya.lunch.api.v2.dalla.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dalla")
public class DallaController {

    private final DallaService dallaService;
    private final Response response;

    @PostMapping("/heart")
    public ResponseEntity<Response.Body> heart(){
        dallaService.heart();
        return response.success(Code.SEARCH_SUCCESS);
    }

    @PostMapping("/attendance")
    public ResponseEntity<Response.Body> attendance(){
        dallaService.attendance();
        return response.success(Code.SEARCH_SUCCESS);
    }
}
