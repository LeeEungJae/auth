package com.gradle.auth.auth_server.controller;

import com.gradle.auth.auth_server.dto.UserInfoDto;
import com.gradle.auth.auth_server.service.impl.UserServiceImpl;
import com.gradle.auth.auth_server.vo.UserResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/api/user")
    public String signUp(@RequestBody UserInfoDto userInfo) {
        System.out.println(userInfo);
        userServiceImpl.save(userInfo);
        return "redirect:/login";
    }

    @GetMapping("/api/test")
    public String test() {
        System.out.println("테스트중입니다.");
        return "redirect:/login";
    }

    @PostMapping("/api/login")
    public UserResult signIn(@RequestBody UserInfoDto userInfo) {
        UserResult userResult = new UserResult();
        return userResult;
    }

}
