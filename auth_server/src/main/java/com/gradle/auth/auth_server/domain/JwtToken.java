package com.gradle.auth.auth_server.domain;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@RedisHash("token")
@Data
public class JwtToken {

    private String username;
    private String token;
}
