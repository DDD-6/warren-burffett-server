package com.warrenbuffett.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "user_consume_main")
public class UserConsumeMain extends BaseTime{
    @Id
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = EAGER)
    private User user;

    @Column
    private String etc;

    @Column
    private int cost;

    @Column
    @Enumerated(EnumType.STRING)
    private UserConsumeType userConsumeType;

    @Builder
    public UserConsumeMain(final Long id, User user, String etc, int cost, UserConsumeType userConsumeType) {
        this.id = id;
        this.user = user;
        this.etc = etc;
        this.cost = cost;
        this.userConsumeType = userConsumeType;

    }

}
