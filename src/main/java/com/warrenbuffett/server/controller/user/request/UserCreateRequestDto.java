package com.warrenbuffett.server.controller.user.request;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message="NAME_IS_MANDATORY")
    private String name;
    @NotBlank(message="PASSWORD_IS_MANDATORY")
    private String password;
    @NotBlank(message="EMAIL_IS_MANDATORY")
    @Email(message = "NOT_VALID_EMAIL")
    private String email;

    public User toEntity(){
        return User.builder()
                .user_name(name)
                .email(email)
                .password(password)
                .userOauthType(UserOauthType.LOCAL)
                .build();
    }
}