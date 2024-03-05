package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.common.exception.AuthorityException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.Role;
import com.yeoboya.lunch.config.security.repository.MemberRolesRepository;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import com.yeoboya.lunch.config.security.reqeust.RoleRequest;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import com.yeoboya.lunch.api.v1.member.response.MemberRoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    private final UserService userService;
    private final MemberRepository memberRepository;
    private final MemberRolesRepository memberRolesRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final Response response;

    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    //fixme
    public ResponseEntity<Response.Body> updateAuthority(RoleRequest roleRequest) {

        //권한 부여받을 멤버
        Member targetMember = memberRepository.findByEmail(roleRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("No authentication information."));

        Role role = roleRepository.findByRole(roleRequest.getRole());

        // 권한이 이미 있는지 확인
        Optional<MemberRole> memberRoles = memberRolesRepository.findByMemberEmail(targetMember.getEmail());

        if(memberRoles.isPresent()) {
            // 이미 존재하므로 업데이트
        } else {
            // 존재하지 않으므로 생성
            MemberRole.createMemberRoles(targetMember, role));
            memberRolesRepository.save(memberRoles);
        }


//        String token = jwtTokenProvider.resolveToken(request);
//        Authentication authentication = jwtTokenProvider.getAuthentication(token);
//        String redisRT = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        return null;
    }

    public ResponseEntity<Response.Body> getAuthorityList(Pageable pageable) {
        Page<MemberRoleResponse> withRolesInPages = memberRepository.findWithRolesInPages(pageable);
        return response.success(Code.SEARCH_SUCCESS, withRolesInPages);
    }

    @Transactional
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public void createRole(Role role){
        roleRepository.save(role);
    }


}
