package com.db.gourmetguide.service;

import com.db.gourmetguide.model.News;
import com.db.gourmetguide.model.UserComment;
import com.db.gourmetguide.model.UserLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
@RequiredArgsConstructor
public class NewsService {
    //support for editing news
    public void editNews(News news) {
        News editedNews;
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/news/" + news.getId()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            editedNews = objectMapper.readValue(response.body(), News.class);
            editedNews.setPinned(news.isPinned());
            editedNews.setImage(news.getImage());
            editedNews.setText(news.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //support for deleting news
    public void deleteNews(Integer newsId) {
//        groupNewsRepository.deleteByNewsId(newsId);
//        List<UserComment> userComments = commentRepository.getAllByNewsId(newsId);
//        List<UserLike> userLikes = likeRepository.getAllByNewsId(newsId);
//        for(UserComment userComment : userComments) {
//            commentRepository.deleteByNewsId(userComment.getId());
//        }
//
//        for(UserLike userLike : userLikes) {
//           likeRepository.deleteByNewsId(userLike.getId());
//        }
//
//        newsRepository.deleteById(newsId);
//    }
        return;
    }
}
