package com.warrenbuffett.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Table(name = "badge")
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String image;

    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Builder
    public Badge(final Long id, final String title, final String image, final Challenge challenge) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.challenge = challenge;
    }
}
