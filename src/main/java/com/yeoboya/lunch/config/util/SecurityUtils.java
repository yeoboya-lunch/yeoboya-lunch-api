package com.yeoboya.lunch.config.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 현재 인증된 사용자의 이메일을 반환합니다.
     *
     * @return 현재 사용자의 이메일
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // 일반적으로 username은 이메일입니다.
        }
        return null;
    }

    /**
     * 현재 사용자가 주어진 이메일과 일치하는지 확인합니다.
     *
     * @param email 비교할 이메일
     * @return 현재 사용자가 주어진 이메일과 일치하면 true, 그렇지 않으면 false
     */
    public static boolean isCurrentUser(String email) {
        String currentUserEmail = getCurrentUserEmail();
        return currentUserEmail != null && currentUserEmail.equals(email);
    }
}
