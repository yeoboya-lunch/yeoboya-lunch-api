package com.yeoboya.guinGujik.api.member.repository;

import com.yeoboya.guinGujik.api.member.dto.Users;
import com.yeoboya.guinGujik.config.constants.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UsersRepository {
    public void login(){

    }

    public boolean existsByEmail(String email) {
        return email.equals("inforex@gmail.com");
    }

    public void save(Users user) {
        //db
        log.warn("회원가입 성공 : {}", user);
    }

    public Optional<Users> findByEmail(String email) {
        List<String> role = new ArrayList<>();
        role.add(Authority.ROLE_USER.name());
        Users users = new Users("khjzzm@gmail.com", "qwer1234@@", role);
        return Optional.ofNullable(users);
    }

}