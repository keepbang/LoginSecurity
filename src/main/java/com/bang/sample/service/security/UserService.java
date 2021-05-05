package com.bang.sample.service.security;

import com.bang.sample.model.User;
import com.bang.sample.repository.UserRepository;
import com.bang.sample.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String save(User user, boolean isOwner) {

        String role = "";

        if(isOwner){
            role = "ROLE_OWNER";
        }else{
            role = "ROLE_USER";
        }


        return userRepository.save(User.builder()
                .id(UUID.randomUUID().toString())
                .email(user.getEmail())
                .nickname(StringUtils.hasText(user.getNickname())?user.getNickname():user.getEmail())
                .enabled(!isOwner)
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(role)) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    public String login(User user) {
        User member = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        if(!member.isEnabled()){
            throw new IllegalArgumentException("관리자의 승인이 필요합니다.");
        }

        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }


    @Transactional
    public void delete(String id){
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        userRepository.delete(member);
    }

}
