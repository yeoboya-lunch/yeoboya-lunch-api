package com.yeoboya.lunch.api.v1._sample.controller;

import com.yeoboya.lunch.api.v1._sample.model.vo.Greet;
import com.yeoboya.lunch.api.v1._sample.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/v1/caffeine")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    Map<Long, Greet> greetVOMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Greet koreaGreet = Greet.builder()
                .id(1L)
                .country("한국")
                .say("안녕하세요")
                .build();

        Greet chinaGreet = Greet.builder()
                .id(2L)
                .country("중국")
                .say("Ni hao")
                .build();

        Greet japanGreet = Greet.builder()
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
    public List<Greet> greetList() throws InterruptedException {
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

    @DeleteMapping("/delete-cache")
    public String deleteCache(String id) {
        return cacheService.removeCache(id);
    }

    @DeleteMapping("/all-cache")
    public String deleteAllCache() {
        return cacheService.removeAllCache();
    }

}
