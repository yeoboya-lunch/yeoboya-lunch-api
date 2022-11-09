package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.dto.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UsersRepository {

    public void update(Users user){
        user.getRoles().add(Authority.ROLE_ADMIN.name());
        log.warn("회원업데이트 성공 : {}", user);
    }

    public Optional<Users> findByEmail(String email) {
        List<String> role = new ArrayList<>();
        role.add(Authority.ROLE_USER.name());
        if(!email.equals("khjzzm@gmail.com")){
            return Optional.empty();
        }else{
            Users users = new Users(email, "qwer1234@@", role);
            return Optional.ofNullable(users);
        }
    }

}