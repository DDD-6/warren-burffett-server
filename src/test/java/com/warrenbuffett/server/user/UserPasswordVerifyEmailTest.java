package com.warrenbuffett.server.user;

import com.warrenbuffett.server.ServerApplicationTests;
import com.warrenbuffett.server.service.UserPasswordResetVerifyEmailService;
import javassist.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserPasswordVerifyEmailTest extends ServerApplicationTests{

    @Autowired
    private UserPasswordResetVerifyEmailService passwordResetVerifyEmailService;


    @Test
    @DisplayName("인증링크 생성 및 전송")
    void emailGenerateTest() throws NotFoundException {
        passwordResetVerifyEmailService.sendVerificationMail("rachel3486@gmail.com");
    }

    @Test
    @DisplayName("등록되지 않은 인증링크")
    void verifyEmailFailTest() throws NotFoundException {
        String key ="32135f38-aae1-4e38-97da-2a0df6";
        passwordResetVerifyEmailService.verifyEmail(key);
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmailSuccessTest() throws NotFoundException {
        String key = "32135f38-aae1-4e38-97da-2c49648a0df6";
        passwordResetVerifyEmailService.verifyEmail(key);
    }
}

