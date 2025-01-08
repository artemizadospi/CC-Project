package com.db.gourmetguide.repository;

import com.db.gourmetguide.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Integer> {
    List<UserFollow>  findByUserId(Integer userId);

}
