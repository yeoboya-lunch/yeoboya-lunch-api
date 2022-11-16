package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.config.common.Response;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.Roles;
import com.yeoboya.lunch.config.security.dto.Token;
import com.yeoboya.lunch.config.security.repository.MemberRolesRepository;
import com.yeoboya.lunch.config.security.repository.RolesRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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
public class UsersService {

    private final MemberRepository memberRepository;
    private final RolesRepository rolesRepository;
    private final MemberRolesRepository memberRolesRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    //fixme
    public ResponseEntity<?> signUp(UserRequest.SignUp signUp) {
        if (memberRepository.findByEmail(signUp.getEmail()).isPresent()) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

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

        //save member
        Member saveMember = Member.createMember(build, memberRoles);

        Member save = memberRepository.save(saveMember);
        MemberResponse memberResponse = new MemberResponse(save);

        return response.success(memberResponse, "회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> login(UserRequest.Login login) {
        if (memberRepository.findByEmail(login.getEmail()).isEmpty()) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(login.toAuthentication());
        Token token = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                token.getRefreshToken(),
                token.getRefreshTokenExpirationTime() - new Date().getTime(),
                TimeUnit.MILLISECONDS);

        return response.success(token, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(UserRequest.Reissue reissue) {
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token is not.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String redisRT = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if (ObjectUtils.isEmpty(redisRT)) {
            return response.fail("Invalid request.", HttpStatus.BAD_REQUEST);
        }

        if (!redisRT.equals(reissue.getRefreshToken())) {
            return response.fail("refresh token does not match.", HttpStatus.BAD_REQUEST);
        }

        Token token = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                token.getRefreshToken(),
                token.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS);

        return response.success(token, "Token has been updated.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(UserRequest.Logout logout) {
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("Invalid request.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        if (!redisTemplate.opsForValue().get("RT:" + authentication.getName()).isEmpty()) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        //add redis blacklist
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue().set(logout.getAccessToken(),
                "logout",
                expiration,
                TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    @Transactional
    //fixme parameter
    public ResponseEntity<?> authority(HttpServletRequest request) {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        String userEmail = JwtTokenProvider.getCurrentUserEmail();

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
        Roles roles = rolesRepository.findByRole(Authority.ROLE_ADMIN);

        MemberRole saveMemberRole = MemberRole.createMemberRoles(member, roles);
        memberRolesRepository.save(saveMemberRole);

        // token 갱신
        String token = jwtTokenProvider.resolveToken(request);
        UserRequest.Reissue reissue = new UserRequest.Reissue();
        reissue.setAccessToken(token);
//        reissue.setRefreshToken();
        this.reissue(reissue);

        return response.success();
    }

}