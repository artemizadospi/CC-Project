package com.db.gourmetguide.controller;

import com.db.gourmetguide.dtos.*;
import com.db.gourmetguide.exception.NewsNotFoundException;
import com.db.gourmetguide.model.News;
import com.db.gourmetguide.model.User;
import com.db.gourmetguide.model.UserComment;
import com.db.gourmetguide.model.UserLike;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private User getLoggedInUser(String jwt) {
        String[] split_string = jwt.split("\\.");
        String base64EncodedBody = split_string[1];
        String body = new String(Base64.getDecoder().decode(base64EncodedBody));
        JSONObject jsonObject = new JSONObject(body);
        String username = (String) jsonObject.get("username");
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/users/" + username))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            User user = objectMapper.readValue(response.body(), User.class);
            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ResponseNewsDTO> getNewsDTOsById(int id) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/news/all"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            List<News> newsList = objectMapper.readValue(response.body(), new TypeReference<List<News>>() {
            });

            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/count"))
                    .GET()
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long count = objectMapper.readValue(response.body(), long.class);
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            return newsList.stream()
                    .filter(news -> news.getId() == id)
                    .map(news -> new ResponseNewsDTO(news.getId(), news.getPublisher().getLastName() + " " +
                            news.getPublisher().getFirstName(), news.getPublishDate(), news.getText(), news.getImage(),
                            news.isPinned(), news.getLikes(), news.getComments(), news.getTopics(), news.getCop(), count))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("")
    public Page<ResponseNewsDTO> getAllNews(@PageableDefault(size = 4) final Pageable pageable) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            RestTemplate restTemplate = new RestTemplate();
            String responseBodyJson = restTemplate.postForObject("http://localhost:8081/news/all/pageable/" + pageNumber + "/" + pageSize,
                    "", String.class);
            PageNewsDTO<News> newsPage = objectMapper.readValue(responseBodyJson, new TypeReference<PageNewsDTO<News>>() {
            });

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/count"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long count = objectMapper.readValue(response.body(), long.class);
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            Page<ResponseNewsDTO> responseNewsPage = newsPage.map(news -> new ResponseNewsDTO(
                    news.getId(),
                    news.getPublisher().getLastName() + " " + news.getPublisher().getFirstName(),
                    news.getPublishDate(),
                    news.getText(),
                    news.getImage(),
                    news.isPinned(),
                    news.getLikes(),
                    news.getComments(),
                    news.getTopics(),
                    news.getCop(),
                    count
            ));

            if (responseNewsPage.isEmpty()) {
                throw new NewsNotFoundException();
            }

            return responseNewsPage;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseNewsDTO getNewsById(@PathVariable(name = "id") int id) {
        List<ResponseNewsDTO> rezNews = getNewsDTOsById(id);
        if (rezNews.isEmpty()) {
            throw new NewsNotFoundException();
        }

        return rezNews.get(0);
    }

    @GetMapping("/filter")
    public List<ResponseNewsDTO> getAllNewsByTopic(@RequestParam(name = "topics") List<String> topics) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/news/all"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            List<News> newsList = objectMapper.readValue(response.body(), new TypeReference<List<News>>() {
            });

            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/count"))
                    .GET()
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long count = objectMapper.readValue(response.body(), long.class);
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            List<ResponseNewsDTO> rezNews = newsList.stream()
                    .filter(news -> news.getTopics().containsAll(topics))
                    .map(news -> new ResponseNewsDTO(news.getId(), news.getPublisher().getLastName() + " " +
                            news.getPublisher().getFirstName(), news.getPublishDate(), news.getText(), news.getImage(),
                            news.isPinned(), news.getLikes(), news.getComments(), news.getTopics(), news.getCop(), count))
                    .collect(Collectors.toList());

            if (rezNews.isEmpty()) {
                throw new NewsNotFoundException();
            }

            return rezNews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteNewsById(@PathVariable(name = "id") int id) {
        HttpClient httpClient = HttpClient.newHttpClient();
        List<ResponseNewsDTO> rezNews = getNewsDTOsById(id);
        if (rezNews.isEmpty()) {
            throw new NewsNotFoundException();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/news/" + id))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/{id}/pin")
    public ResponseNewsDTO pinNewsById(@PathVariable(name = "id") int id) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/news/" + id))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            News rezNews = objectMapper.readValue(response.body(), News.class);

            if (rezNews == null) {
                throw new NewsNotFoundException();
            }

            rezNews.setPinned(!rezNews.isPinned());
            requestBody = objectMapper.writeValueAsString(rezNews);
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/save"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/count"))
                    .GET()
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long count = objectMapper.readValue(response.body(), long.class);
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            return new ResponseNewsDTO(rezNews.getId(), rezNews.getPublisher().getLastName() + " " +
                    rezNews.getPublisher().getFirstName(), rezNews.getPublishDate(), rezNews.getText(), rezNews.getImage(),
                    rezNews.isPinned(), rezNews.getLikes(), rezNews.getComments(), rezNews.getTopics(), rezNews.getCop(), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/new")
    public ResponseNewsDTO createNews(
            @RequestParam("text") String text,
            @RequestParam("topics") Set<String> topicsJson,
            @RequestParam("cop") String cop,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest http) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;

        try {
            News createdNews = new News();
            createdNews.setText(text);
            if (image != null) {
                createdNews.setImage(image.getBytes());
            }
            createdNews.setPublishDate(new Date());
            createdNews.setTopics(topicsJson);
            createdNews.setCop(cop);
            createdNews.setLikedByCurrentUser(false);
            User user = getLoggedInUser(http.getHeader("Authorization"));
            createdNews.setPublisher(user);

            requestBody = objectMapper.writeValueAsString(createdNews);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/save"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

//            request = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:8081/news/count"))
//                    .GET()
//                    .build();
//
//            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long count = 0;
//            System.out.println("Response code: " + response.statusCode());
//            System.out.println("Response body: " + response.body());

            return new ResponseNewsDTO(createdNews.getId(), createdNews.getPublisher().getLastName() + " " +
                    createdNews.getPublisher().getFirstName(), createdNews.getPublishDate(),
                    createdNews.getText(), createdNews.getImage(), createdNews.isPinned(), createdNews.getLikes(),
                    createdNews.getComments(), createdNews.getTopics(), createdNews.getCop(), count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseNewsDTO editNews(@PathVariable(name = "id") int id, @RequestParam("image") MultipartFile image,
                                    @RequestBody CreateAndEditNewsDTO newNews) throws IOException {
//        News rezNews = newsRepository.getNewsById(id);
//
//        if (rezNews == null) {
//            throw new NewsNotFoundException();
//        }
//        if (!newNews.getText().equals("")) {
//            rezNews.setText(newNews.getText());
//        }
//        if (image != null) {
//            rezNews.setImage(image.getBytes());
//        }
//        if (!newNews.getTopics().isEmpty()) {
//            rezNews.setTopics(newNews.getTopics());
//        }
//        rezNews.setPublishDate(new Date());
//
//        newsRepository.save(rezNews);
//
//        User user = userRepository.getUserById(rezNews.getPublisher().getId());
//
//        return new ResponseNewsDTO(rezNews.getId(), user.getLastName() + " " + user.getFirstName(),
//                rezNews.getPublishDate(), rezNews.getText(), rezNews.getImage(), rezNews.isPinned(), rezNews.getLikes(),
//                rezNews.getComments(), rezNews.getTopics(), rezNews.getCop(), newsRepository.count());
        return null;
    }

    @GetMapping("/{id}/comments")
    public Page<ResponseCommentDTO> getNewsCommentsById(@PathVariable(name = "id") int id, @PageableDefault(size = 4) final Pageable pageable) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse<String> response;
        HttpRequest request;

        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/" + id))
                    .GET()
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            News rezNews = objectMapper.readValue(response.body(), News.class);

            if (rezNews == null) {
                throw new NewsNotFoundException();
            }

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            String responseBodyJson = restTemplate.postForObject("http://localhost:8081/comment/pageable/" + rezNews.getId() + "/" + pageNumber + "/" + pageSize,
                    new HttpEntity<>("", headers), String.class);
            PageCommentDTO<UserComment> commentsPage = objectMapper.readValue(responseBodyJson, new TypeReference<PageCommentDTO<UserComment>>() {
            });

            Page<ResponseCommentDTO> responseCommentsPage = commentsPage.map(comments -> {
                HttpClient httpC = HttpClient.newHttpClient();
                ObjectMapper objM = new ObjectMapper();
                HttpResponse<String> resp;
                HttpRequest req;

                req = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8081/users/getUserById/" + comments.getUserId()))
                        .GET()
                        .build();

                User user = null;
                try {
                    resp = httpC.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Response code: " + resp.statusCode());
                    System.out.println("Response body: " + resp.body());
                    user = objM.readValue(resp.body(), User.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResponseCommentDTO(
                        user.getLastName() + " " + user.getFirstName(),
                        comments.getText(), commentsPage.getTotalElements()
                );
            });
            return responseCommentsPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/{id}/likes")
    public List<String> getNewsLikesById(@PathVariable(name = "id") int id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/news/" + id;
        try {
            URI uri = URI.create(url);
            ResponseEntity<News> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<News>() {
            });
            News rezNews = responseEntity.getBody();
            if (rezNews == null) {
                throw new NewsNotFoundException();
            }
            return rezNews.getLikes().stream()
                .map(like -> {
                    String getUserByIdURL = "http://localhost:8081/users/getUserById/" + like.getUserId();
                    URI getUserByIdUri = URI.create(getUserByIdURL);
                    ResponseEntity<User> responseUserEntity = restTemplate.exchange(getUserByIdUri, HttpMethod.GET, null, new ParameterizedTypeReference<User>() {
                    });
                    User user  = responseUserEntity.getBody();
                    return user.getLastName() + " " +
                                user.getFirstName();
                }).collect(Collectors.toList());


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/{id}/like")
    public ResponseNewsDTO likeNewsById(@PathVariable(name = "id") int id, HttpServletRequest http) {
        String url = "http://localhost:8081/news/" + id;
        String getAllNewsURL = "http://localhost:8081/news/all";
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            News rezNews = objectMapper.readValue(response.body(), News.class);
            if (rezNews == null) {
                throw new NewsNotFoundException();
            }

            User user = getLoggedInUser(http.getHeader("Authorization"));
            if (rezNews.getLikes().stream().anyMatch(like -> like.getUserId() == user.getId())) {
                rezNews.getLikes().remove(rezNews.getLikes().stream().filter(like -> like.getUserId() == user.getId()).findFirst().orElseThrow(Exception::new));
            } else {
                UserLike userLike = new UserLike();
                userLike.setUserId(user.getId());
                userLike.setNews(rezNews);
            }
            String requestBody = objectMapper.writeValueAsString(rezNews);
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/news/save"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            URI getAllNewsUri = URI.create(getAllNewsURL);
            HttpRequest request3 = HttpRequest.newBuilder()
                    .uri(getAllNewsUri)
                    .GET()
                    .build();
            HttpResponse<String> response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            List<News> allNews = objectMapper.readValue(response3.body(), new TypeReference<List<News>>(){});
            return new ResponseNewsDTO(rezNews.getId(), rezNews.getPublisher().getLastName() + " " +
                    rezNews.getPublisher().getFirstName(), rezNews.getPublishDate(), rezNews.getText(), rezNews.getImage(),
                    rezNews.isPinned(), rezNews.getLikes(), rezNews.getComments(), rezNews.getTopics(), rezNews.getCop(), allNews.size());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/{id}/comment")
    public ResponseNewsDTO commentOnNewsById(@PathVariable(name = "id") int id, @RequestBody String comment, HttpServletRequest http) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/news/" + id;
        String getAllNewsURL = "http://localhost:8081/news/all";
        String saveURL = "http://localhost:8081/news/save";
        try {
            URI uri = URI.create(url);
            ResponseEntity<News> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<News>() {
            });
            News rezNews = responseEntity.getBody();
            if (rezNews == null) {
                throw new NewsNotFoundException();
            }

            User user = getLoggedInUser(http.getHeader("Authorization"));
            UserComment userComment = new UserComment();
            userComment.setUserId(user.getId());
            userComment.setNews(rezNews);
            userComment.setText(comment);
            userComment.setDate(new Date());

            rezNews.getComments().add(userComment);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            String rezNewsJson;
            try {
                rezNewsJson = objectMapper.writeValueAsString(rezNews);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
            URI getAllNewsUri = URI.create(getAllNewsURL);
            ResponseEntity<List<News>> allNewsResponseEntity = restTemplate.exchange(getAllNewsUri, HttpMethod.GET, null, new ParameterizedTypeReference<List<News>>() {});
            List<News> allNews = allNewsResponseEntity.getBody();

            HttpEntity<String> requestEntity = new HttpEntity<>(rezNewsJson, headers);
            ResponseEntity<News> responsePostEntity = restTemplate.exchange(saveURL, HttpMethod.POST, requestEntity, News.class);
            return new ResponseNewsDTO(rezNews.getId(), rezNews.getPublisher().getLastName() + " " +
                    rezNews.getPublisher().getFirstName(), rezNews.getPublishDate(), rezNews.getText(), rezNews.getImage(),
                    rezNews.isPinned(), rezNews.getLikes(), rezNews.getComments(), rezNews.getTopics(), rezNews.getCop(), allNews.size());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/CoP/{cop}")
    public Page<ResponseNewsDTO> getAllNewsByCop(@PathVariable(name = "cop") String cop, @PageableDefault(size = 4) final Pageable pageable) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/news/all";
        String findByIdInUrl = "http://localhost:8081/news/";

        try {
            URI uri = URI.create(url);
            ResponseEntity<List<News>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<News>>() {});
            List<News> newsList = responseEntity.getBody();
            if (newsList != null) {
                List<Integer> rezNewsIds = newsList.stream()
                        .filter(news -> news.getCop().equals(cop))
                        .map(News::getId)
                        .collect(Collectors.toList());
                List<String> stringIds = rezNewsIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                URI findByIdInUri  = UriComponentsBuilder.fromHttpUrl(findByIdInUrl)
                        .queryParam("ids", String.join(",", stringIds))
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build()
                        .toUri();
                ResponseEntity<PageNewsDTO<News>> findByIdInResponse = restTemplate.exchange(findByIdInUri, HttpMethod.GET, null, new ParameterizedTypeReference<PageNewsDTO<News>>() {});
                PageNewsDTO<News> pageNewsDTO = findByIdInResponse .getBody();
                System.out.println(pageNewsDTO);
                return pageNewsDTO
                        .map(news -> new ResponseNewsDTO(news.getId(), news.getPublisher().getLastName() + " " +
                                news.getPublisher().getFirstName(), news.getPublishDate(), news.getText(), news.getImage(),
                                news.isPinned(), news.getLikes(), news.getComments(), news.getTopics(), news.getCop(), rezNewsIds.size()));
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/search/{contains}")
    public Page<ResponseNewsDTO> getNewsContaining(@PathVariable(name = "contains") String contains,
                                                   Pageable pageable) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/news/search/" + contains +
                "?page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize();

        try {
            URI uri = URI.create(url);
            ResponseEntity<PageNewsDTO<News>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<PageNewsDTO<News>>() {});
            PageNewsDTO<News> pageNewsDTO = responseEntity.getBody();

            Page<ResponseNewsDTO> responseNewsPage = pageNewsDTO.map(news -> new ResponseNewsDTO(
                    news.getId(),
                    news.getPublisher().getLastName() + " " + news.getPublisher().getFirstName(),
                    news.getPublishDate(),
                    news.getText(),
                    news.getImage(),
                    news.isPinned(),
                    news.getLikes(),
                    news.getComments(),
                    news.getTopics(),
                    news.getCop(),
                    pageNewsDTO.getTotalElements()
            ));

            if (responseNewsPage.isEmpty()) {
                Page.empty();
            }

            return responseNewsPage;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
