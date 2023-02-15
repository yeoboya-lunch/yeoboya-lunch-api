package com.yeoboya.lunch.api.io;

import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.IntStream;

@SpringBootTest
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
public class ShopServiceTest {


    @Autowired
    ShopService service;

    @Test
    void create() {

        ShopCreate shopCreate = new ShopCreate();
        IntStream.rangeClosed(1, 300).forEach(i->{
            shopCreate.setShopName(i+"번 방");
            service.create(shopCreate);
        });
    }
}
