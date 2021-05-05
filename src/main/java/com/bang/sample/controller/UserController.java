package com.bang.sample.controller;


import com.bang.sample.model.ResultStatus;
import com.bang.sample.model.User;
import com.bang.sample.service.security.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join/user")
    public String join(@RequestBody User user) {
        return userService.save(user, false);
    }

    //OWNER 회원가
    @PostMapping("/join/owner")
    public String ownerJoin(@RequestBody User user) {
        return userService.save(user, true);
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user);
    }

    //로그아웃은 client에서 token정보를 삭제해주며노

    //회원정보수정

    //회원탈퇴(삭제)
    @DeleteMapping("/user/{id}")
    public ResponseEntity<ResultStatus> deleteUser(@PathVariable String id){
        userService.delete(id);

        return new ResponseEntity<>(ResultStatus.SUCCESS, HttpStatus.OK);
    }

}