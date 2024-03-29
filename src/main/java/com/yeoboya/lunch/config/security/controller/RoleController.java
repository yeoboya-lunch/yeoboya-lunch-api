package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.config.security.reqeust.AuthorityRequest;
import com.yeoboya.lunch.config.security.reqeust.SecurityRequest;
import com.yeoboya.lunch.config.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 권한추가/수정
     */
    @PostMapping("/authority-update")
    public ResponseEntity<Response.Body> updateAuthority(@RequestBody AuthorityRequest authorityRequest) {
        return roleService.updateAuthority(authorityRequest);
    }

    /**
     * 회원 권한리스트
     */
    @GetMapping("/authorities")
    public ResponseEntity<Response.Body> getAuthorityList(Pageable pageable){
        return roleService.getAuthorityList(pageable);
    }

    /**
     * 계정 잠금 수정
     */
    @PostMapping("/security-update")
    public ResponseEntity<Response.Body> updateSecurity(@RequestBody SecurityRequest securityRequest){
        return roleService.updateSecurityStatus(securityRequest);
    }

}
