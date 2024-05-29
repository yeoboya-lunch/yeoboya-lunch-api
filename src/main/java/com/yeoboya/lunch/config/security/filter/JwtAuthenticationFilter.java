package com.yeoboya.lunch.config.security.filter;

import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.repository.TokenIgnoreUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenIgnoreUrlRepository tokenIgnoreUrlRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (!shouldIgnore(request) && jwtTokenProvider.validateToken(token)) {
            String isLogout = redisTemplate.opsForValue().get("LOT:" + token);
            if (ObjectUtils.isEmpty(isLogout)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("=================================  토큰 컨텍스트에서 통과 정보  ============================================");
                log.debug(authentication.getPrincipal() + " : " + authentication);
                log.debug(token);
                log.debug("=====================================================================================================");
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldIgnore(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        String uri = request.getRequestURI();

        return tokenIgnoreUrlRepository.getTokenIgnoreUrls()
                .stream()
                .anyMatch(r -> matcher.match(r.getUrl(), uri) && Boolean.TRUE.equals(r.getIsIgnore()));
    }

}
