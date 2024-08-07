package ru.terentyev.EffectiveMobileTestTask.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private SecretKey secretKey = Jwts.SIG.HS256.key().build();
	
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey) 
                .compact();
    }

    
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    
    private Date extractExpiration(String token) {
        return Jwts.parser() 
                .verifyWith(secretKey) 
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    
    public String extractUsername(String token) {
        return Jwts.parser() 
        		.verifyWith(secretKey) 
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}