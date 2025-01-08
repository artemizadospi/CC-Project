package com.db.gourmetguide.dtos;

import com.db.gourmetguide.model.UserComment;
import com.db.gourmetguide.model.UserLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseNewsDTO {
    private int id;
    private String publisher;
    public Date publishDate;
    private String text;
    private byte[] image;
    private boolean pinned;
    private List<UserLike> likes;
    private Set<UserComment> comments;
    private Set<String> topics;
    private String cop;
    private long totalPosts;

    public ResponseNewsDTO(int id, String s, Date publishDate, String text, byte[] image, boolean pinned, List<UserLike> likes, Set<UserComment> comments, Set<String> topics, String cop) {
    }
}
