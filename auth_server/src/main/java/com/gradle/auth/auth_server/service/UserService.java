package com.gradle.auth.auth_server.service;

import com.gradle.auth.auth_server.dto.UserInfoDto;

public interface UserService {
    Long save(UserInfoDto infoDto);
}
