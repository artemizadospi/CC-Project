package com.db.gourmetguide.controller;

import com.db.gourmetguide.model.UserComment;
import com.db.gourmetguide.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentRepository commentRepository;

    @GetMapping("/{newsId}")
    List<UserComment> getAllByNewsId(@PathVariable Integer newsId) {
        return commentRepository.getAllByNewsId(newsId);
    }

    @PostMapping("/pageable/{newsId}/{page}/{size}")
    Page<UserComment> getAllCommentsByNewsId(@PathVariable Integer newsId, @PathVariable Integer page, @PathVariable Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.getAllCommentsByNewsId(newsId, pageable);
    }

    @DeleteMapping("/{newsId}")
    List<UserComment> deleteByNewsId(@PathVariable Integer newsId) {
        return commentRepository.deleteByNewsId(newsId);
    }
}
