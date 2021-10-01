package com.warrenbuffett.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import static javax.persistence.FetchType.EAGER;


@Entity
@NoArgsConstructor
@ToString
@Table(name = "user_income_main")
@Getter
public class UserIncomeMain extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = EAGER)
    private User user;

    @Column
    private int payday; // 급여일

    @Column
    private int salary; // 급여

    @Column
    private int week_day;

    @Column
    private int working_hours;


    @Builder
    public UserIncomeMain(User user, final Long id, int payday,int salary,int week_day, int working_hours) {
        this.user = user;
        this.payday = payday;
        this.salary = salary;
        this.week_day = week_day;
        this.working_hours = working_hours;
    }

}