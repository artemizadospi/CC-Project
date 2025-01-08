package com.db.gourmetguide.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user_comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class UserComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int userId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
    @Column(nullable = false)
    private String text;
    //TO DO: creation date
    @Column(nullable = false)
    private Date date;
}
