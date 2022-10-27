package com.yeoboya.guinGujik.config.security.service;

import com.yeoboya.guinGujik.config.common.Response;
import com.yeoboya.guinGujik.config.security.JwtTokenProvider;
import com.yeoboya.guinGujik.config.constants.Authority;
import com.yeoboya.guinGujik.config.security.domain.Member;
import com.yeoboya.guinGujik.config.security.dto.Token;
import com.yeoboya.guinGujik.config.security.dto.Users;
import com.yeoboya.guinGujik.config.security.dto.reqeust.UserRequest;
import com.yeoboya.guinGujik.config.security.repository.UsersJpaRepository;
import com.yeoboya.guinGujik.config.security.repository.UsersRepository;
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
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersJpaRepository usersJpaRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<?> signUp(UserRequest.SignUp signUp) {
        if (usersJpaRepository.findByEmail(signUp.getEmail()).isPresent()) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = Member.builder().
                email(signUp.getEmail()).
                password(passwordEncoder.encode(signUp.getPassword())).
                role(Authority.ROLE_USER).build();
        return response.success(usersJpaRepository.save(member), "회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> login(UserRequest.Login login) {
        if (!usersJpaRepository.findByEmail(login.getEmail()).isPresent()) {
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

    public ResponseEntity<?> authority() {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        String userEmail = JwtTokenProvider.getCurrentUserEmail();

        Users user = usersRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        usersRepository.update(user);

        return response.success();
    }

}