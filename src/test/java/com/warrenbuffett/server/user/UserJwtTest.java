package com.warrenbuffett.server.user;

import com.warrenbuffett.server.ServerApplicationTests;
import com.warrenbuffett.server.jwt.JwtTokenProvider;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import io.jsonwebtoken.Claims;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserJwtTest extends ServerApplicationTests {

    @Autowired
    private JwtTokenProvider jwtManager;
//
//    @BeforeEach
//    void setUp() {
//        jwtManager = new JwtTokenProvider();
//    }
//
//    @Test
//    @DisplayName("토큰 생성 및 복호화 테스트")
//    void tokenTest() {
//        LocalDateTime now = LocalDateTime.now();
//        final User user = User.builder()
//                .user_name("tobby")
//                .image("image")
//                .password("password")
//                .email("email@naver.com")
//                .userOauthType(UserOauthType.LOCAL)
//                .build();
//        final String token = jwtManager.generateToken(user);
//        String email = jwtManager.getUserEmailFromToken(token);
//        Claims data = jwtManager.getTokenData(token);
//        System.out.println(token);
//        System.out.println(data);
//        MatcherAssert.assertThat(email,is("email@naver.com"));
//    }
}

