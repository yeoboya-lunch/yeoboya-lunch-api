package com.yeoboya.lunch.config.security.filter;

import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.domain.TokenIgnoreUrl;
import com.yeoboya.lunch.config.security.repository.TokenIgnoreUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final TokenIgnoreUrlRepository tokenIgnoreUrlRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && !shouldIgnore(request) && jwtTokenProvider.validateToken(token)) {
            String isLogout = (String) redisTemplate.opsForValue().get("LOT:" + token);
            if (ObjectUtils.isEmpty(isLogout)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("=================================  토큰 컨텍스트에서 통과 정보  ============================================");
                log.info(authentication.getPrincipal() + " : " + authentication);
                log.info(token);
                log.info("=====================================================================================================");
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnore(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return tokenIgnoreUrlRepository.getTokenIgnoreUrls()
                .stream()
                .anyMatch(tokenIgnoreUrl ->
                        uri.equals(tokenIgnoreUrl.getUrl())
                                && Boolean.TRUE.equals(tokenIgnoreUrl.getIsIgnore()));
    }

}
