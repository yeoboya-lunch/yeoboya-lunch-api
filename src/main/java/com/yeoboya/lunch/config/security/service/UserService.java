package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.Roles;
import com.yeoboya.lunch.config.security.dto.Token;
import com.yeoboya.lunch.config.security.repository.MemberRolesRepository;
import com.yeoboya.lunch.config.security.repository.RolesRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final RolesRepository rolesRepository;
    private final MemberRolesRepository memberRolesRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<Body> signUp(SignUp signUp) {

        // member
        Member build = Member.builder()
                .email(signUp.getEmail())
                .name(signUp.getName())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .build();

        // roles
        Roles roles = rolesRepository.findByRole(Authority.ROLE_USER);

        // member_roles
        List<MemberRole> memberRoles = new ArrayList<>();
        memberRoles.add(MemberRole.createMemberRoles(build, roles));

        // save member
        Member saveMember = Member.createMember(build, memberRoles);

        memberRepository.save(saveMember);
        return response.success(Code.SAVE_SUCCESS);
    }

    public ResponseEntity<Body> signIn(SignIn signIn) {
        if (memberRepository.findByEmail(signIn.getEmail()).isEmpty()) {
            return response.fail(ErrorCode.USER_NOT_FOUND);
        }

        // loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(signIn.toAuthentication());
        Token token = jwtTokenProvider.generateToken(authentication);

        // save redis refreshToken
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                token.getRefreshToken(),
                token.getRefreshTokenExpirationTime() - new Date().getTime(),
                TimeUnit.MILLISECONDS);

        return response.success(Code.SEARCH_SUCCESS, token);
    }

    public ResponseEntity<Body> signOut(SignOut signOut) {
        if (!jwtTokenProvider.validateToken(signOut.getAccessToken())) {
            return response.fail(ErrorCode.INVALID_AUTH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(signOut.getAccessToken());

        //fixme null check
        if (!redisTemplate.opsForValue().get("RT:" + authentication.getName()).isEmpty()) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        //add redis blacklist
        Long expiration = jwtTokenProvider.getExpiration(signOut.getAccessToken());
        redisTemplate.opsForValue().set(signOut.getAccessToken(),
                "logout",
                expiration,
                TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<Body> reIssue(Reissue reissue) {
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String redisRT = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if (ObjectUtils.isEmpty(redisRT)) {
            return response.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!redisRT.equals(reissue.getRefreshToken())) {
            return response.fail(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Token token = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                token.getRefreshToken(),
                token.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS);

        return response.success(Code.UPDATE_SUCCESS, token);
    }


    @Transactional
    public ResponseEntity<Body> changePassword(Credentials credentials) {
        Member member = memberRepository.findByEmail(credentials.getEmail()).
                orElseThrow(()->new EntityNotFoundException("Member not found - " + credentials.getEmail()));

        if (!passwordEncoder.matches(credentials.getOldPassword(), member.getPassword())){
            return response.fail(ErrorCode.INVALID_OLD_PASSWORD);
        }

        if (!credentials.getNewPassword().equals(credentials.getConfirmNewPassword())){
            return response.fail(ErrorCode.INVALID_PASSWORD);
        }

        member.setPassword(passwordEncoder.encode(credentials.getNewPassword()));

        return response.success(Code.UPDATE_SUCCESS);
    }


    @Transactional
    public ResponseEntity<Body> resetPassword(Credentials credentials) {
        Member member = memberRepository.findByEmail(credentials.getEmail()).
                orElseThrow(()->new EntityNotFoundException("Member not found - " + credentials.getEmail()));

        if (!credentials.getNewPassword().equals(credentials.getConfirmNewPassword())){
            return response.fail(ErrorCode.INVALID_PASSWORD);
        }

        member.setPassword(passwordEncoder.encode(credentials.getNewPassword()));

        return response.success(Code.UPDATE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Body> authority(HttpServletRequest request) {
        String userEmail = JwtTokenProvider.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        Roles roles = rolesRepository.findByRole(Authority.ROLE_ADMIN);

        MemberRole saveMemberRole = MemberRole.createMemberRoles(member, roles);
        memberRolesRepository.save(saveMemberRole);

        String token = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String redisRT = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        return this.reIssue(new Reissue(token, redisRT));
    }

}