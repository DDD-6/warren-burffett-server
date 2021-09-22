package com.warrenbuffett.server.domain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@ToString
@Table(name = "user_income_side")
@Getter
public class UserIncomeSide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JoinColumn(name = "umi_id")
    private UserIncomeMain userIncomeMain;

    @Enumerated(EnumType.STRING)
    @Column
    private UserIncomeSideType userIncomeSideType;

    @Column
    private int working_time;

    @Column
    private int working_case;

    @Column(nullable = false)
    private int cost;

    @Builder
    public UserIncomeSide(final Long id,UserIncomeMain userIncomeMain, int working_time, int working_case, int cost) {
        this.id=id;
        this.userIncomeMain = userIncomeMain;
        this.working_time = working_time;
        this.working_case = working_case;
        this.cost = cost;
    }
}
