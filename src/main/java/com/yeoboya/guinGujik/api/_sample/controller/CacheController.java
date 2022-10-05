package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api._sample.model.vo.GreetVO;
import com.yeoboya.guinGujik.api._sample.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/caffeine")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    Map<Long, GreetVO> greetVOMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        GreetVO koreaGreet = GreetVO.builder()
                .id(1L)
                .country("한국")
                .say("안녕하세요")
                .build();

        GreetVO chinaGreet = GreetVO.builder()
                .id(2L)
                .country("중국")
                .say("Ni hao")
                .build();

        GreetVO japanGreet = GreetVO.builder()
                .id(3L)
                .country("일본")
                .say("こんにちは。")
                .build();

        greetVOMap.put(koreaGreet.getId(), koreaGreet);
        greetVOMap.put(chinaGreet.getId(), chinaGreet);
        greetVOMap.put(japanGreet.getId(), japanGreet);

    }

    @Cacheable(cacheNames = "greet")
    @GetMapping("/greet")
    public List<GreetVO> greetList() throws InterruptedException {
        Thread.sleep(10000);
        Set<Long> country = greetVOMap.keySet();

        return country.stream()
                .map(userId -> greetVOMap.get(userId))
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "random")
    @GetMapping("/random")
    public String getRandom(String id) {
        return id + "의 캐시값 = " + (Math.random() * 100);
    }

    @GetMapping("/delete-cache")
    public String deleteCache(String id) {
        return cacheService.removeCache(id);
    }

    @GetMapping("/delete-all-cache")
    public String deleteAllCache() {
        return cacheService.removeAllCache();
    }

}
