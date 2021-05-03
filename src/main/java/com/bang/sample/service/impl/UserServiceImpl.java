package com.bang.sample.service.impl;

import com.bang.sample.model.User;
import com.bang.sample.repository.UserRepository;
import com.bang.sample.security.JwtTokenProvider;
import com.bang.sample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long save(User user, boolean isOwner) {

        String role = "";

        if(isOwner){
            role = "ROLE_OWNER";
        }else{
            role = "ROLE_USER";
        }


        return userRepository.save(User.builder()
                .email(user.getEmail())
                .nickname(StringUtils.hasText(user.getNickname())?user.getNickname():user.getEmail())
                .enabled(!isOwner)
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(role)) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    @Override
    public String login(User user) {
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
