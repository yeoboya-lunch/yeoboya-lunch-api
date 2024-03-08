package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.config.security.reqeust.RoleRequest;
import com.yeoboya.lunch.config.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/authority")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 권한추가/수정
     */
    @PostMapping("/update")
    public ResponseEntity<Response.Body> updateAuthority(@RequestBody RoleRequest roleRequest) {
        return roleService.updateAuthority(roleRequest);
    }

    /**
     * 회원 권한리스트
     */
    @GetMapping
    public ResponseEntity<Response.Body> getAuthorityList(Pageable pageable){
        return roleService.getAuthorityList(pageable);
    }

}
