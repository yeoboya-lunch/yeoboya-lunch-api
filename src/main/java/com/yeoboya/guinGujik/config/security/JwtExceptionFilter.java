package com.yeoboya.guinGujik.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
        } catch (JwtException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse httpServletResponse, Throwable ex) throws IOException {
        httpServletResponse.setStatus(status.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ex.getMessage());
            os.flush();
        }
    }
}