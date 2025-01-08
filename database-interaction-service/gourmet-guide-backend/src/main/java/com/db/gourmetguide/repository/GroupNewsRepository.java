package com.db.gourmetguide.repository;

import com.db.gourmetguide.model.GroupNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupNewsRepository extends JpaRepository<GroupNews, Integer> {
    int deleteByIdAndNewsId(Integer groupId, Integer newsId);
    void deleteByNewsId(Integer newsId);
    List<GroupNews> findGroupNewsById(Integer groupId);
    Optional<GroupNews> findGroupNewsByNewsId(Integer newsId);

}
