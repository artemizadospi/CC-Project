package com.db.gourmetguide.repository;

import com.db.gourmetguide.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    News getNewsById(Integer id);
    void deleteById(Integer id);
    Page<News> findAllByIdIn(List<Integer> ids, Pageable pageable);
    @Query(value = "select n from News n where n.text like %:contains%")
    Page<News> getNewsContaining(@Param("contains") String contains, Pageable pageable);
}
