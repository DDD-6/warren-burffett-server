package com.warrenbuffett.server.controller.user;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import lombok.*;

import javax.management.relation.Role;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String image;
    private UserOauthType userOauthType;

    public User toEntity(){
        return User.builder()
                .user_name(name)
                .email(email)
                .password(password)
                .image(image)
                .userOauthType(userOauthType)
                .build();
    }
}