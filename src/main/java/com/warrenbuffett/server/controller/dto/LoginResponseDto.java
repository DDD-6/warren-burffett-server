package com.warrenbuffett.server.controller.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final TokenDto accessToken;

    public LoginResponseDto(TokenDto accessToken) {
        this.accessToken = accessToken;
    }
}