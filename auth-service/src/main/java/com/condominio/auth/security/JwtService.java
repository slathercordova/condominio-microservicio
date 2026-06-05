package com.condominio.auth.security;

import com.condominio.auth.auth.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.long-expiration}")
    private long longExpiration;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UsuarioEntity usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("userId", usuario.getId().toString())
                .claim("type","access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String generateRefreshToken(UsuarioEntity usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("userId", usuario.getId().toString())
                .claim("type","refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ longExpiration))
                .signWith(getSignKey())
                .compact();
    }

    public String generatePasswordResetToken(UsuarioEntity usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("userId",usuario.getId().toString())
                .claim("type","password_reset")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 5)) //5 min
                .signWith(getSignKey())
                .compact();
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

    public Instant extractExpiration(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().toInstant();
    }

    public String extractType(String token){
        return extractClaims(token).get("type", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }catch (Exception e){
            return false;
        }
    }

    public boolean isAccessToken(String token){
        return "access".equals(extractType(token));
    }
}
