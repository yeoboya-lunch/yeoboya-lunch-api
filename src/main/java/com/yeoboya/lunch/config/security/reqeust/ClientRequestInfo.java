package com.yeoboya.lunch.config.security.reqeust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequestInfo {
    private String remoteIp;
    private String sessionId;
    private LocalDateTime loginTime;
    private String requestUri;
}
