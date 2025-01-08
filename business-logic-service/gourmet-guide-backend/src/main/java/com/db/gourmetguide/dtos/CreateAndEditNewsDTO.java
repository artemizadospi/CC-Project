package com.db.gourmetguide.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateAndEditNewsDTO {
    private String text;
//    private MultipartFile image;
    private Set<String> topics;
    private String cop;
}
