package com.gradle.auth.auth_server.service.impl;

import com.gradle.auth.auth_server.dto.UserInfoDto;
import com.gradle.auth.auth_server.entity.UserInfo;
import com.gradle.auth.auth_server.entity.UserInfoRepository;
import com.gradle.auth.auth_server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public Long save(UserInfoDto infoDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setPassword(encoder.encode(infoDto.getPassword()));

        return userInfoRepository.save(
                UserInfo.builder().id(infoDto.getId()).auth(infoDto.getAuth()).password(infoDto.getPassword()).build())
                .getCode();
    }

}
