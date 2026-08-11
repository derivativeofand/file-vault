package com.jha58.file_vault.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .subject(username)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build() 
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);

            return extractedUsername.equals(username) && !isTokenExpired(token); 
        } catch (Exception e) {
            return false; 
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}