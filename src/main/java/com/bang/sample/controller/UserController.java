package com.bang.sample.controller;

import com.bang.sample.model.User;
import com.bang.sample.repository.UserRepository;
import com.bang.sample.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody User user) {
        log.info(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(User.builder()
                .email(user.getEmail())
                .enabled(true)
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    //OWNER 회원가
    @PostMapping("/join/owner")
    public Long ownerJoin(@RequestBody User user) {
        return userRepository.save(User.builder()
                .email(user.getEmail())
                .enabled(false) //관리자가 승인을 해줘야
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList("ROLE_OWNER")) // 최초 가입시 OWNER 로 설정
                .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User member = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        log.info("enabled : {}",member.isEnabled());
        if(!member.isEnabled()){
            throw new IllegalArgumentException("관리자의 승인이 필요합니다.");
        }

        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}