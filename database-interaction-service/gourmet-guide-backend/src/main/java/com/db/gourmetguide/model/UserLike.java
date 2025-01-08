package com.db.gourmetguide.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel
@Table(name="user_like")
@Data
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int userId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    public void setNews(News news) {
        this.news = news;
        news.getLikes().add(this);
    }
}
