package com.yeoboya.guinGujik.config.security.service;


import com.yeoboya.guinGujik.config.security.JwtTokenProvider;
import com.yeoboya.guinGujik.config.security.dto.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityUtil(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void saveSecurityContext(UserDetails userDetails) {
        SecurityUser securityUser = (SecurityUser) userDetails;

        // Verify SSO token value
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser.getUsername(),
                securityUser.getPassword(),
                userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }


    public static String getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        return authentication.getName();
    }


}
