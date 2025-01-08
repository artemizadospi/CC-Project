package com.db.gourmetguide.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCommentDTO {
    private String userName;
    private String text;
    private long totalComments;
}
