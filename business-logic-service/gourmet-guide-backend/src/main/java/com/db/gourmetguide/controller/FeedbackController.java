package com.db.gourmetguide.controller;

import com.db.gourmetguide.model.Feedback;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @PostMapping
    public Feedback saveFeedback(@RequestBody Feedback feedback) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;

        try {
            requestBody = objectMapper.writeValueAsString(feedback);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/feedback"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            return objectMapper.readValue(response.body(), Feedback.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/feedback"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            List<Feedback> feedbacks = objectMapper.readValue(response.body(), new TypeReference<List<Feedback>>() {
            });
            return feedbacks;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

