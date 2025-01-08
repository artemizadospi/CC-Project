package com.db.gourmetguide.controller;

import com.db.gourmetguide.model.Role;
import com.db.gourmetguide.model.User;
import com.db.gourmetguide.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/{username}")
    public User getByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @PostMapping("")
    public void updateUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @PostMapping("/signup")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id){
        return userRepository.getUserById(id);
    }
}
