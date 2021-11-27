package com.warrenbuffett.server.controller.dto;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String image;
    private String user_name;
    private String email;
}