package com.example.hospital_backend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.Base64;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000; // 15분
    private final long REFRESH_TOKEN_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000; // 7일

    public JwtTokenProvider(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // JWT 서명 키를 URL-safe Base64로 인코딩
    private String getEncodedSecretKey() {
        return Base64.getUrlEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_VALIDITY_MS);
    }

    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_VALIDITY_MS);
    }

    private String createToken(String username, long validityMs) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMs);

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(getEncodedSecretKey())), SignatureAlgorithm.HS256)
            .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(getEncodedSecretKey())))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid or expired JWT token: " + e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UserDetails loadUserByToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(getEncodedSecretKey())))
            .build()
            .parseClaimsJws(token)
            .getBody();
        String username = claims.getSubject();
        return userDetailsService.loadUserByUsername(username);
    }
}
