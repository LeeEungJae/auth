package com.gradle.auth.auth_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {
    private String id;
    private String password;
    private String auth;

}
