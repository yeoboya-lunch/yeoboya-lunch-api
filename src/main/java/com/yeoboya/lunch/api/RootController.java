package com.yeoboya.lunch.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> getRootInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("serviceName", "yeoboya-lunch-api");
        info.put("version", "1.0.0");
        info.put("serviceStatus", "active");
        info.put("lastUpdated", "2024-03-21");
        info.put("mainWebsite", "https://yeoboya-lunch.com");
        info.put("api-documentation", "http://localhost:8080/docs/index.html");
        info.put("github", "https://github.com/yeoboya-lunch");
        return info;
    }

}
