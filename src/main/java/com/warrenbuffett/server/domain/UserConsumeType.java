package com.warrenbuffett.server.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserConsumeType {
    transportation,
    food,
    entertainment,
    apparel,
    health_care,
    communication,
    education
}
