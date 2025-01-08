package com.db.gourmetguide.controller;

import com.db.gourmetguide.exception.NewsNotFoundException;
import com.db.gourmetguide.model.News;
import com.db.gourmetguide.model.User;
import com.db.gourmetguide.model.UserComment;
import com.db.gourmetguide.model.UserLike;
import com.db.gourmetguide.repository.CommentRepository;
import com.db.gourmetguide.repository.LikeRepository;
import com.db.gourmetguide.repository.NewsRepository;
import com.db.gourmetguide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsRepository newsRepository;

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable(name = "id") int id) {
        return newsRepository.getNewsById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNewsById(@PathVariable(name = "id") int id) {
        newsRepository.deleteById(id);
    }

    @GetMapping("/search/{contains}")
    public Page<News> getNewsContaining(@PathVariable(name = "contains") String contains, @PageableDefault(size=4) final Pageable pageable) {
        return newsRepository.getNewsContaining(contains, pageable);
    }

    @GetMapping("")
    public Page<News> findAllByIdIn(@RequestParam("ids") List<Integer> ids,
                                    Pageable pageable) {
        return newsRepository.findAllByIdIn(ids, pageable);
    }

    @GetMapping("/all")
    public List<News> getAll(){
        return newsRepository.findAll();
    }

    @PostMapping("/save")
    public News save(@RequestBody News news){
        return newsRepository.save(news);
    }

    @PostMapping("/all/pageable/{page}/{size}")
    public Page<News> getAllPageable(@PathVariable Integer page, @PathVariable Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "pinned", "publishDate");
        Pageable sortedPageable = PageRequest.of(page, size, sort);
        return newsRepository.findAll(sortedPageable);
    }

    @GetMapping("/count")
    public long count(){
        return newsRepository.count();
    }


}
