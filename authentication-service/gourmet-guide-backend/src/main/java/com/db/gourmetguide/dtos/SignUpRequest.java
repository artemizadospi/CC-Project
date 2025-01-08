package com.db.gourmetguide.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpRequest {
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private String email;
}
