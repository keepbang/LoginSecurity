package com.bang.sample.controller;


import com.bang.sample.service.security.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TestController {

    private final UserService userService;

    @Secured("ROLE_OWNER")
    @GetMapping(value = "/owner/test")
    public String isAdmin(){
        return "owner 계정입니다.";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/user/test")
    public String isUser(){
        return "user 계정입니다.";
    }
}
