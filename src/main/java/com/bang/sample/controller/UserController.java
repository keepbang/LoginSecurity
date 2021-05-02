package com.bang.sample.controller;


import com.bang.sample.model.User;
import com.bang.sample.repository.UserRepository;
import com.bang.sample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody User user) {
        return userService.save(user, false);
    }

    //OWNER 회원가
    @PostMapping("/join/owner")
    public Long ownerJoin(@RequestBody User user) {
        return userService.save(user, true);
    }

    // 로그인
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user);
    }
}