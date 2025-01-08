package com.db.gourmetguide.repository;

import com.db.gourmetguide.model.UserComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository  extends JpaRepository<UserComment, Integer> {
    List<UserComment> getAllByNewsId(Integer newsId);
    Page<UserComment> getAllCommentsByNewsId(Integer newsId, Pageable pageable);

    List<UserComment> deleteByNewsId(Integer newsId);

}