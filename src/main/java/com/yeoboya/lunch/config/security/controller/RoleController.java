package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.config.security.reqeust.RoleRequest;
import com.yeoboya.lunch.config.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/authority")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 권한추가
     */
    @PostMapping("/add")
    public ResponseEntity<Response.Body> authority(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        return roleService.authority(roleRequest, request);
    }


}
