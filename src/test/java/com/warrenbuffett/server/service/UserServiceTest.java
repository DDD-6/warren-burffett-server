package com.warrenbuffett.server.service;

import com.warrenbuffett.server.ServerApplicationTests;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


public class UserServiceTest extends ServerApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void create() {
        String name = "test";
        String email = "test";
        String password = "test";
        UserOauthType userOauthType = UserOauthType.KAKAO;

        User user = new User().builder().user_name(name).password(password).email(email).userOauthType(userOauthType).build();
        userService.createUser(user);

    }

    @Test
    public void serchById() {
        User user = userService.searchUser(1L);
        System.out.println(user);
    }
}
