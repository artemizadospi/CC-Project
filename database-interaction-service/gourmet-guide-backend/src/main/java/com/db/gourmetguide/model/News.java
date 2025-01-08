package com.db.gourmetguide.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty("id")
    private int id;
    @Column
    @JsonProperty("image")
    private byte[] image;
    //may contain links
    @Column
    @JsonProperty("title")
    private String title;
    @Column
    @JsonProperty("text")
    private String text;
    @Column(nullable = false, name = "is_pinned")
    @JsonProperty("pinned")
    private boolean pinned;
    //TO DO: add relationship to user entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty("publisher")
    private User publisher;
    @Column
    @JsonProperty("publishDate")
    private Date publishDate;
    @Column(name = "topics")
    @ElementCollection
    @CollectionTable(name = "news_topics", joinColumns = @JoinColumn(name = "id"))
    @JsonProperty("topics")
    private Set<String> topics;
    @JsonManagedReference
    @OneToMany(targetEntity = UserLike.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("likes")
    private List<UserLike> likes;
    @JsonManagedReference
    @OneToMany(targetEntity = UserComment.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("comments")
    private Set<UserComment> comments;
    @Column
    @JsonProperty("cop")
    private String cop;
    @Column(name = "current_user_like")
    @JsonProperty("likedByCurrentUser")
    private boolean likedByCurrentUser;
}