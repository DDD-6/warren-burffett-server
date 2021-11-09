package com.warrenbuffett.server.controller.user;

import com.warrenbuffett.server.domain.UserOauthType;
import org.springframework.beans.BeanUtils;
import com.warrenbuffett.server.domain.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String user_name;
    private String email;
    private String image;
    private UserOauthType userOauthType;

    public UserResponseDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}