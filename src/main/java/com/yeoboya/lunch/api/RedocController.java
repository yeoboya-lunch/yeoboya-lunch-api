package com.yeoboya.lunch.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedocController {

    @GetMapping("/redoc")
    public String redoc() {
        System.out.println("redoc start!");
        return "redirect:/redoc.html"; // `/redoc` URL에서 자동으로 `redoc.html`로 이동
    }
}