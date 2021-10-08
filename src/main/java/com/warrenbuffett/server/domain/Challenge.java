package com.warrenbuffett.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@ToString
@Table(name="challenge")
@Getter
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false)
    private LocalDate end_date;


    @Column(nullable = false)
    private String goal;


    @Builder
    public Challenge(final Long id, final String title, final String content,final String goal, final LocalDate start_date, final LocalDate end_date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.goal = goal;
    }
}
