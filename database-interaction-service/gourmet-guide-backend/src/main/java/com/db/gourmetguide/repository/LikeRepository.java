package com.db.gourmetguide.repository;

import com.db.gourmetguide.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<UserLike, Integer> {
    List<UserLike> getAllByNewsId(Integer newsId);
    List<UserLike> getAllByUserId(Integer userId);
    UserLike findByNewsIdAndUserId(Integer userId, Integer NewsId);
    void deleteById(Integer likeId);
    List<UserLike> deleteByNewsId(Integer newsId);

}
