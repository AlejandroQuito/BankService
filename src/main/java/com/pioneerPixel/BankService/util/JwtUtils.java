package com.pioneerPixel.BankService.util;

import com.pioneerPixel.BankService.exception.JwtAuthException;
import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.security.SecurityConstants;
import com.pioneerPixel.BankService.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final SecurityConstants securityConstants;
    private final CustomUserDetailsService userDetailsService;

    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
                securityConstants.getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        return generateToken(userDetails, securityConstants.getAccessLifetime());
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return generateToken(userDetails, securityConstants.getRefreshLifetime());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtAuthException("Jwt token is expired or invalid");
        }
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(securityConstants.getAuthHeader());
    }

    private String generateToken(CustomUserDetails userDetails, Integer lifetime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId().toString());

        Date issuedDate = new Date();
        Date expiryDate = new Date(issuedDate.getTime() + lifetime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
