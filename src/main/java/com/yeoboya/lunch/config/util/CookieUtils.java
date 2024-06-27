package com.yeoboya.lunch.config.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static Cookie createSecureHttpOnlyCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    public static void addCookieToResponse(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }
}
