package com.yeoboya.lunch.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> getRootInfo() {
        return Map.of(
            "version", "1.0.0",
            "documentation", "http://example.com/docs"
        );
    }

}
