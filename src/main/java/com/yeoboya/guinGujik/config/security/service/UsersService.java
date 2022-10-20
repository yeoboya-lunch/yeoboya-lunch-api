package com.yeoboya.guinGujik.config.security.service;

import com.yeoboya.guinGujik.config.common.Response;
import com.yeoboya.guinGujik.config.constants.Authority;
import com.yeoboya.guinGujik.config.security.JwtTokenProvider;
import com.yeoboya.guinGujik.config.security.domain.Member;
import com.yeoboya.guinGujik.config.security.dto.Token;
import com.yeoboya.guinGujik.config.security.dto.Users;
import com.yeoboya.guinGujik.config.security.dto.reqeust.UserRequest;
import com.yeoboya.guinGujik.config.security.repository.UsersJpaRepository;
import com.yeoboya.guinGujik.config.security.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(login.toAuthentication());

        // 인증 정보를 기반으로 JWT 토큰 생성
        Token token = jwtTokenProvider.generateToken(authentication);

        // redis

        return response.success(token, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(UserRequest.Reissue reissue) {

        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // fixme DB 에서 user 의 Refresh Token 값을 가져와야함
        String refreshToken = reissue.getRefreshToken();

        // (추가) 로그아웃되어 DB 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        Token token = jwtTokenProvider.generateToken(authentication);

        return response.success(token, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(UserRequest.Logout logout) {

        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

//         Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // fixme DB 에서 email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());


        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<?> authority() {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        String userEmail = jwtTokenProvider.getCurrentUserEmail();

        Users user = usersRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        usersRepository.update(user);

        return response.success();
    }
}