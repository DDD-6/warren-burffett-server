package com.warrenbuffett.server.config.auth;

import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.domain.UserOauthType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String image;
    private UserOauthType userOauthType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String image,UserOauthType userOauthType) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.image = image;
        this.userOauthType = userOauthType;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if( registrationId.equals("kakao")) return ofKakao(userNameAttributeName, attributes);
        else if( registrationId.equals("naver")) return ofNaver(userNameAttributeName, attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .image((String) attributes.get("picture"))
                .userOauthType(UserOauthType.GOOGLE)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .image((String) response.get("profile_image"))
                .userOauthType(UserOauthType.NAVER)
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }
    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String,Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile");
        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .image((String) profile.get("profile_image_url"))
                .userOauthType(UserOauthType.KAKAO)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public User toEntity() {
        return User.builder()
                .user_name(name)
                .email(email)
                .image(image)
                .userOauthType(userOauthType)
                .build();
    }
}