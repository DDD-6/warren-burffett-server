package com.warrenbuffett.server.user;

import com.warrenbuffett.server.ServerApplicationTests;
import com.warrenbuffett.server.controller.user.response.UserResponseDto;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import com.warrenbuffett.server.repository.UserRepository;
import com.warrenbuffett.server.service.UserService;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserServiceTest extends ServerApplicationTests {

    @Autowired
    private UserService userService;
    private UserRepository userRepository;

    @After
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void findAll() {
        List<UserResponseDto> userList = userService.findAll();
        for (UserResponseDto user:userList) {
            System.out.println(user.getUser_name());
        }
    }

    @Test
    public void create() {
        String name = "test";
        String email = "test@gmail.com";
        String password = "test";
        String image = "test";
        UserOauthType userOauthType = UserOauthType.KAKAO;

        User user = new User().builder()
                        .user_name(name)
                        .image(image)
                        .password(password)
                        .email(email)
                        .userOauthType(userOauthType)
                        .build();
        userService.createUser(user);

    }

    @Test
    public void serchById() {
        User user = userService.searchUser(2L);
        MatcherAssert.assertThat(user.getUser_name(),is("test"));
    }

    @Test
    @Order(4)
    public void delete() {
        User user = userService.searchUser(1L);
        MatcherAssert.assertThat(userService.deleteUser(user.getId()),is(true));
    }
}
