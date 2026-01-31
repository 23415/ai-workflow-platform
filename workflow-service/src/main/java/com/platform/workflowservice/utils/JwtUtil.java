package com.platform.workflowservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(
            @Value("${jwt.secret:}") String secret
    ) {
        if (secret.isBlank()) {
            throw new IllegalStateException("jwt.secret is EMPTY or NOT LOADED");
        }

        this.key = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secret)
        );
    }

    public Claims validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }
}
