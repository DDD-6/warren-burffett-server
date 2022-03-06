package com.warrenbuffett.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserOauthType {
    KAKAO,
    NAVER,
    GOOGLE,
    LOCAL;
}
