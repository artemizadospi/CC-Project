package com.db.gourmetguide.service;

import com.db.gourmetguide.dtos.SignUpRequest;
import com.db.gourmetguide.exception.UserCreationException;
import com.db.gourmetguide.exception.UserNotFoundException;
import com.db.gourmetguide.model.Role;
import com.db.gourmetguide.model.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    public User getByUsername(String username) throws UserNotFoundException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/users/" + username))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 404) {
                throw new UserNotFoundException();
            }
            String responseBody = response.body();

             ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, User.class);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    public void updateUser(User user) {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;

        try {
            requestBody = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            throw new UserCreationException("Failed to serialize user object to JSON");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/users"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new UserCreationException("Failed to create user: " + e.getMessage());
        }
    }

    public User addUser(SignUpRequest signUpRequest) {
        HttpClient httpClient = HttpClient.newHttpClient();
        User user = new User();

        System.out.println(signUpRequest.getFirstName());
        System.out.println("ok");

        user.setLastName(signUpRequest.getLastName());
        user.setFirstName(signUpRequest.getFirstName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.user);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            throw new UserCreationException("Failed to serialize user object to JSON");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/users/signup"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);

            return objectMapper.readValue(responseBody, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserCreationException("Failed to create user: " + e.getMessage());
        }
    }
}
