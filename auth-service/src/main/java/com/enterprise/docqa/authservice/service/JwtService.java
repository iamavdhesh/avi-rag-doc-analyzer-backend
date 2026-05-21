package com.enterprise.docqa.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String signingKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public String createAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, signingKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(SignatureAlgorithm.HS512, signingKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Authentication buildAuthentication(String token) {
        Claims claims = verify(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        return new UsernamePasswordAuthenticationToken(username, token, List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase().replace(' ', '_'))));
    }

    public Claims verify(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
