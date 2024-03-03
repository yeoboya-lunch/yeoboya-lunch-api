package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.common.exception.AuthorityException;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.service.EmailService;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.Roles;
import com.yeoboya.lunch.config.security.repository.MemberRolesRepository;
import com.yeoboya.lunch.config.security.repository.RolesRepository;
import com.yeoboya.lunch.config.security.reqeust.RoleRequest;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    private final UserService userService;
    private final MemberRepository memberRepository;
    private final MemberRolesRepository memberRolesRepository;
    private final RolesRepository rolesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response.Body> authority(RoleRequest roleRequest, HttpServletRequest request) {

        //매니저 여부 확인
        String adminEmail = JwtTokenProvider.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(adminEmail).orElseThrow(
                () -> new UsernameNotFoundException("No authentication information."));

        List<MemberRole> memberRoles = memberRolesRepository.findByMemberEmail(member.getEmail());
        boolean hasAdminRole = memberRoles.stream()
                .map(MemberRole::getRoles)
                .map(Roles::getRole)
                .anyMatch(role -> role == Authority.ROLE_ADMIN);

        if(!hasAdminRole){
            throw new AuthorityException("User does not have the necessary authority");
        }

        //권한 부여받을 멤버
        Member targetMember = memberRepository.findByEmail(roleRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("No authentication information."));

        Roles roles = rolesRepository.findByRole(roleRequest.getRole());

        if(roles.getRole().equals(Authority.ROLE_ADMIN)){
            throw new AuthorityException("Admin role cannot be modified");
        }

        MemberRole saveMemberRole = MemberRole.createMemberRoles(targetMember, roles);
        memberRolesRepository.save(saveMemberRole);

        String token = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String redisRT = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        return userService.reissue(new UserRequest.Reissue(token, redisRT));
    }
}
