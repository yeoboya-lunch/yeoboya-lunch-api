package com.yeoboya.lunch.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Disabled
@SpringBootTest
class RedisUtilTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    @DisplayName("string 저장 (key-value)")
    void string_redis() {
        String key = "string-key02";

        String value = "str_value-02";
        redisUtil.setStringOps(key, value, 1, TimeUnit.HOURS);
        assertEquals("str_value-02", redisUtil.getStringOps(key), ()-> "값이 일치 하지 않는다.");
    }


    @Test
    @DisplayName("list 저장 (key-list)")
    void list_redis() {
        redisUtil.delAsterOps("list-key02");
        String key = "list-key02";

        List<String> strings = Arrays.asList("list-value-01", "list-value-02");
        redisUtil.setListOps(key, strings);

        List<String> listOps = redisUtil.getListOps(key);
        listOps.forEach(System.out::println);

        assertEquals(strings, listOps);
    }

    @Test
    @DisplayName("hash 저장 (key-hash)")
    void hash_redis() {
        String key = "hash-key02";

        HashMap<String, String> map = new HashMap<String, String>() {
            {
                put("key_01", "value-01");
                put("key_02", "value-02");
            }
        };
        redisUtil.setHashOps(key, map);

        assertEquals("value-01", redisUtil.getHashOps(key, "key_01"));
        assertEquals("value-02", redisUtil.getHashOps(key, "key_02"));
    }


    @Test
    @DisplayName("set 저장 (key-set)")
    void set_redis() {
        String key = "set-key02";

        redisUtil.setSetOps(key, "value_1","value_2","value_1");

        Set<String> setOps = redisUtil.getSetOps(key);

        setOps.forEach(System.out::println);
    }

    @Test
    @DisplayName("sort set 저장 (key-List<Struct.SortedSet>")
    void sortedSet_redis() {
        String key = "sort-key02";

        List<Struct.SortedSet> values = new ArrayList<>();
        values.add(new Struct.SortedSet(){{
            setValue("sortedSet_value_100");
            setScore(100D);
        }});
        values.add(new Struct.SortedSet(){{
            setValue("sortedSet_value_10");
            setScore(10D);
        }});
        redisUtil.setSortedSetOps(key, values);

        var sortedSetOps = redisUtil.getSortedSetOps(key);

        sortedSetOps.forEach(System.out::println);

    }


    @Test
    @DisplayName("*02 키 조회")
    void searchKey(){
        List<String> asterOps = redisUtil.getAsterOps("*02");

        asterOps.forEach(System.out::println);
    }

}