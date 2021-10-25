package com.warrenbuffett.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserIncomeSideType {
    overtime,
    part_time_job,
    per_case_job,
    irregular_income
}
