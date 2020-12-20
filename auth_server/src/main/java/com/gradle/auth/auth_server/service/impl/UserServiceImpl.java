package com.gradle.auth.auth_server.service.impl;

import com.gradle.auth.auth_server.dto.UserInfoDto;
import com.gradle.auth.auth_server.entity.UserInfo;
import com.gradle.auth.auth_server.entity.UserInfoRepository;
import com.gradle.auth.auth_server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo loadUserByUsername(String email) throws UsernameNotFoundException {
        return userInfoRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException((email)));
    }

    public Long save(UserInfoDto infoDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setPassword(encoder.encode(infoDto.getPassword()));

        return userInfoRepository.save(UserInfo.builder().email(infoDto.getEmail()).auth(infoDto.getAuth())
                .password(infoDto.getPassword()).build()).getCode();
    }

    @Transactional
    @Override
    public int deleteId(String email) {

        int num = userInfoRepository.deleteByEmail(email);

        return num;

    }

}
