package com.gradle.auth.auth_server.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private String email;
    private String password;
    private String auth;

}
