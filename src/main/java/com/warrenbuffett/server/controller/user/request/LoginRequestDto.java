package com.warrenbuffett.server.controller.user.request;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import lombok.*;

@Getter
@Setter
public class LoginRequestDto {

    private String email;
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}