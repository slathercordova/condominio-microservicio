package com.condominio.edificio.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public UUID extractUserId(String token){
        Claims claims = extractClaims(token);

        return UUID.fromString(claims.get("userId", String.class));
    }

    public String extractType(String token){
        return extractClaims(token).get("type", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            String username = extractUsername(token);
            return (username != null && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token){
        return "access".equals(extractType(token));
    }
}
