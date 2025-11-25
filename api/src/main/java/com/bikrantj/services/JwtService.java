package com.bikrantj.services;  // Adjust to your package

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {
    // Put this at the top of JwtService.java
    private static final String FIXED_SECRET = "mySuperSecretKey1234567890123456789012345678901234567890"; // at least 32 chars

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(FIXED_SECRET.getBytes());
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30;  // 30 days in milliseconds

    /**
     * Generates a signed JWT containing the admin's data.
     */
    public static String generateToken(String adminId, String username, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(adminId)  // Admin ID as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    public static String getAdminId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static String getEmail(String token) {
        return extractClaim(token, claims -> (String) claims.get("email"));
    }


    public static boolean isValid(String token) {
        try {
            Jwts.parser()  // Returns JwtParserBuilder directly in 0.12.x
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException | io.jsonwebtoken.ExpiredJwtException | IllegalArgumentException e) {
            return false;  // Invalid signature, expired, or malformed
        }
    }


    public static String getUsername(String token) {
        return extractClaim(token, claims -> (String) claims.get("username"));
    }


    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private static Claims extractAllClaims(String token) {
        return Jwts.parser()  // Returns JwtParserBuilder directly in 0.12.x
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}