package com.warrenbuffett.server.domain;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@Table(name="user")
@Getter
public class User extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user_name;

    @Column
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String image;

    @Enumerated(EnumType.STRING)  // 직접 case처리 or enumerated?
    @Column
    private UserOauthType userOauthType;

    @Builder
    public User(final Long id, final String user_name, final String email,final String image, final String password, UserOauthType userOauthType) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.image = image;
        this.userOauthType = userOauthType;
    }

    public User update(String user_name,String image) {
        this.user_name = user_name;
        this.image = image;
        return this;
    }
}
