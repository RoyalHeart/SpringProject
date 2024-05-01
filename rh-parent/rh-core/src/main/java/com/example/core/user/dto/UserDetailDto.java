package com.example.core.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {

    private String username;
    private String language;
    private Long accountId;
    private Long companyId;
}
