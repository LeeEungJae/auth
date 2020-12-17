package com.gradle.auth.auth_server.controller;

import java.util.HashMap;
import java.util.Map;

import com.gradle.auth.auth_server.component.JwtTokenEncoder;
import com.gradle.auth.auth_server.dto.UserInfoDto;
import com.gradle.auth.auth_server.entity.UserInfoRepository;
import com.gradle.auth.auth_server.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private UserServiceImpl userServiceImpl;
    private UserInfoRepository userInfoRepository;
    private JwtTokenEncoder jwtTokenEncoder;
    private AuthenticationManager am;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserInfoRepository userInfoRepository,
            JwtTokenEncoder jwtTokenEncoder, AuthenticationManager am, RedisTemplate<String, Object> redisTemplate) {
        this.userServiceImpl = userServiceImpl;
        this.userInfoRepository = userInfoRepository;
        this.jwtTokenEncoder = jwtTokenEncoder;
        this.redisTemplate = redisTemplate;
        this.am = am;
    }

    @PostMapping("/api/signup")
    public Map<String, Object> signUp(@RequestBody UserInfoDto userInfo) {
        System.out.println(userInfo);
        String email = userInfo.getEmail();
        Map<String, Object> map = new HashMap<>();
        if (userInfoRepository.findByEmail(email).isEmpty()) {
            if (userInfo.getAuth().equals("admin")) {
                userInfo.setAuth("ROLE_ADMIN");
            } else {
                userInfo.setAuth("ROLE_USER");
            }
            userServiceImpl.save(userInfo);
            map.put("success", true);

            return map;
        } else {
            map.put("duplicated id", false);
            return map;
        }
    }

    @GetMapping("/api/test")
    public String test() {
        System.out.println("테스트중입니다.");
        return "redirect:/login";
    }

    @PostMapping("/api/signin")
    public Map<String, Object> signIn(@RequestBody UserInfoDto userInfo) throws Exception {
        String email = userInfo.getEmail();
        System.out.println("id : " + email + " password : " + userInfo.getPassword());
        try {
            am.authenticate(new UsernamePasswordAuthenticationToken(email, userInfo.getPassword()));
        } catch (Exception e) {
            throw e;
        }

        UserDetails userDetails = userServiceImpl.loadUserByUsername(email);
        String accessToken = jwtTokenEncoder.generateAccessToken(userDetails);
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(email, accessToken);

        Map<String, Object> map = new HashMap<>();
        map.put("success", accessToken);
        return map;
    }

}
