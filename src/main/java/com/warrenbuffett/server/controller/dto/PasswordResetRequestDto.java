package com.warrenbuffett.server.controller.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequestDto {
    private String email;
    private String newpassword;
}
