package com.db.gourmetguide.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class Group {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "group_id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

}
