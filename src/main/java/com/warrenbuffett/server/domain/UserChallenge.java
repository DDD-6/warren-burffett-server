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
@Table(name="user_challenge")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = EAGER)
    private User user;

    @JoinColumn(name = "challenge_id")
    @OneToOne(fetch = EAGER)
    private Challenge challenge;

    @Column(columnDefinition="boolean default false")
    private boolean achieved;

    @Builder
    public UserChallenge(final Long id,User user, Challenge challenge, boolean achieved) {
        this.id=id;
        this.user = user;
        this.challenge = challenge;
        this.achieved = achieved;
    }
}
