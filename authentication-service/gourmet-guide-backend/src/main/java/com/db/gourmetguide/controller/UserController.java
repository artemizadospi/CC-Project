package com.db.gourmetguide.controller;

import com.db.gourmetguide.dtos.LoginRequest;
import com.db.gourmetguide.dtos.LoginResponse;
import com.db.gourmetguide.dtos.SignUpRequest;
import com.db.gourmetguide.exception.InvalidCredentialsException;
import com.db.gourmetguide.model.User;
import com.db.gourmetguide.service.JWTUtils;
import com.db.gourmetguide.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "user login", response = LoginResponse.class, notes = "Returns a response for the user trying to login")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Server error"), @ApiResponse(code = 404, message = "Service not found"), @ApiResponse(code = 401, message = "Bad Request"), @ApiResponse(code = 200, message = "Successful operation", response = LoginResponse.class, responseContainer = "List")})
    @PostMapping("/login")
    public LoginResponse login(@ApiParam(value = "username", required = true, defaultValue = "Joe") @RequestBody LoginRequest loginRequest) {
        BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
        User loginUser = userService.getByUsername(loginRequest.getUsername());
        boolean validPassword = bcryptEncoder.matches(loginRequest.getPassword(), loginUser.getPassword());
        if (!validPassword) {
            throw new InvalidCredentialsException();
        }
        loginUser.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        userService.updateUser(loginUser);
        String token = jwtUtils.generateJWT(loginUser);
        return new LoginResponse(token);
    }

    @PostMapping("/verify")
    public boolean verify(@RequestHeader("jwt") String token) {
        return jwtUtils.validate(token);
    }

    @PostMapping("/signup")
    public User addUser(@RequestBody SignUpRequest signUpRequest) {
        return userService.addUser(signUpRequest);
    }
}
