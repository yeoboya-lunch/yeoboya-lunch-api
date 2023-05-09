package com.yeoboya.lunch.api.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemoryController {

    private final MemoryFinder memoryFinder;

    public MemoryController(MemoryFinder memoryFinder) {
        this.memoryFinder = memoryFinder;
    }

    @GetMapping("/memory")
    public Memory system() {
        Memory memory = memoryFinder.get();
        log.info("memory={}", memory);
        return memory;
    }
}