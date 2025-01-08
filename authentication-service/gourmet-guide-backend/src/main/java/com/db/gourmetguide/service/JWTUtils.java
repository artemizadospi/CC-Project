package com.db.gourmetguide.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.db.gourmetguide.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtils {
    private final Algorithm algorithm = Algorithm.HMAC256("encryptionSecret");

    public String generateJWT(User user) {
        Map<String, String> payloadClaims = new HashMap<>();
        payloadClaims.put("firstName", user.getFirstName());
        payloadClaims.put("lastName", user.getLastName());
        payloadClaims.put("email", user.getEmail());
        payloadClaims.put("username", user.getUsername());
        payloadClaims.put("role", user.getRole().toString());
        payloadClaims.put("id", user.getId() + "");
        return JWT.create()
                .withPayload(payloadClaims)
                .withIssuer("gourmetguide")
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public boolean validate(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            jwtVerifier.verify(jwt);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean validateUser(String username, String jwt) {
        if (!validate(jwt)) {
            return false;
        }
        return JWT.decode(jwt).getSubject().equals(username);
    }
}
