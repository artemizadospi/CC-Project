package com.db.gourmetguide.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;
    @Column(nullable = false)
    @JsonProperty("lastName")
    private String lastName;
    @Column(nullable = false)
    @JsonProperty("firstName")
    private String firstName;
    @Column(nullable = false)
    @JsonProperty("username")
    private String username;
    @Column(nullable = false)
    @JsonProperty("password")
    private String password;
    @Column(nullable = false)
    @JsonProperty("email")
    private String email;
    @Enumerated
    @Column(columnDefinition = "smallint")
    @JsonProperty("role")
    private Role role;
}
