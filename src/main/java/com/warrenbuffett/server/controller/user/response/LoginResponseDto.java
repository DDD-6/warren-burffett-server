package com.warrenbuffett.server.controller.user.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final String accessToken;

    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}