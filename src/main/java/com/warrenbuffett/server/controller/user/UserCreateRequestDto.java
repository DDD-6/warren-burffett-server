package com.warrenbuffett.server.controller.user;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import lombok.*;

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

    public User toEntity(){
        return User.builder()
                .user_name(name)
                .email(email)
                .password(password)
                .image(image)
                .userOauthType(UserOauthType.LOCAL)
                .build();
    }
}