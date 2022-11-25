package com.yeoboya.lunch.config.security.filter;

import com.yeoboya.lunch.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!isIgnore(request)) {
            String token = jwtTokenProvider.resolveToken(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                String isLogout = (String) redisTemplate.opsForValue().get(token);
                if (ObjectUtils.isEmpty(isLogout)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("=================================  토큰 컨텍스트에서 통과 정보  ============================================");
                    log.debug(authentication.getPrincipal() + " : " + authentication);
                    log.debug(token);
                    log.debug("=====================================================================================================");
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean isIgnore(HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean result = false;
        for (String ignoreUri : IGNORE_URLS) {
            if (uri.startsWith(ignoreUri)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private final String[] IGNORE_URLS = {
            "/favicon.ico",
            "/splash"
    };

}