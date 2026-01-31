package com.platform.authservice.jwt;

import com.platform.authservice.entities.Roles;
import com.platform.authservice.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    public JwtUtil(
            @Value("${jwt.secret:}") String secret,
            @Value("${jwt.expiration:0}") long expiration
    ) {
        System.out.println(">>> JWT SECRET VALUE = [" + secret + "]");
        System.out.println(">>> JWT SECRET LENGTH = " + secret.length());

        if (secret.isBlank()) {
            throw new IllegalStateException("jwt.secret is EMPTY or NOT LOADED");
        }

        this.key = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secret)
        );
        this.expiration = expiration;
    }


    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles",
                        user.getRoles()
                                .stream()
                                .map(Roles::getName)
                                .toList()
                )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
