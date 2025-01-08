package com.db.gourmetguide.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String jwt;

    public LoginResponse(String jwt_) {
        jwt = jwt_;
    }
}
