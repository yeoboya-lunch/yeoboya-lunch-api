package com.yeoboya.lunch.config.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {

    public static String getClientIP(HttpServletRequest request) {
        String[] headers = {"Proxy-Client-IP",
        "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR",
                "X-Real-IP", "X-RealIP", "REMOTE_ADDR"};
        String ip = request.getHeader("X-Forwarded-For");

        for (String header : headers) {
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if(ip.equals("0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }

        return ip;
    }
}
