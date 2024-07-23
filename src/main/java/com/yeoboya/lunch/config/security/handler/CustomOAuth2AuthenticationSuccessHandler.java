package com.yeoboya.lunch.config.security.handler;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import com.yeoboya.lunch.config.security.dto.Token;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import com.yeoboya.lunch.config.security.service.OAuth2UserImpl;
import com.yeoboya.lunch.config.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final Environment env;

    @Value("${front.url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Member member = ((OAuth2UserImpl) authentication.getPrincipal()).getMember();

        if (member.getRole().getRole().equals(roleRepository.findByRole(Authority.ROLE_GUEST).getRole())) {
            MemberInfo memberInfo = MemberInfo.createMemberInfo(member);
            UserSecurityStatus userSecurityStatus = UserSecurityStatus.createUserSecurityStatus(member);
            Member saveMember = Member.createMember(member, memberInfo, roleRepository.findByRole(Authority.ROLE_USER), userSecurityStatus);
            memberRepository.save(saveMember);
        }

        boolean isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");
        Token token = jwtTokenProvider.generateToken(authentication, member.getProvider(), member.getLoginId());
        Cookie accessTokenCookie = CookieUtils.createSecureHttpOnlyCookie("AccessToken", token.getAccessToken(), isProd);
        Cookie refreshTokenCookie = CookieUtils.createSecureHttpOnlyCookie("RefreshToken", token.getRefreshToken(), isProd);

        CookieUtils.addCookieToResponse(response, accessTokenCookie, "None");
        CookieUtils.addCookieToResponse(response, refreshTokenCookie, "None");

        redisTemplate.opsForValue().set("RT:" + member.getLoginId(),
                token.getRefreshToken(),
                token.getRefreshTokenExpirationTime() - new Date().getTime(),
                TimeUnit.MILLISECONDS);

        String redirectURL = UriComponentsBuilder.fromUriString(frontUrl)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectURL);

    }
}
