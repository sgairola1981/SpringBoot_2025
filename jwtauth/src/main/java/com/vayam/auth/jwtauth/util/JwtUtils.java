package com.vayam.auth.jwtauth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vayam.auth.jwtauth.repository.BlacklistedTokenRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    // Secret key for signing the token (loaded from application properties)
    private final SecretKey secretKey;

    // Token validity period (e.g., 1 hour = 3600000 ms)
    private final long tokenValidity;
      @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public JwtUtils(@Value("${jwt.secret-key}") String secretKeyBase64, 
                    @Value("${jwt.token-validity}") long tokenValidity) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBase64.getBytes());
        this.tokenValidity = tokenValidity;
    }

    /**
     * Generate a JWT token for the given username.
     *
     * @param username the username to include in the token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validate a token and ensure it matches the provided username.
     *
     * @param token    the JWT token
     * @param username the username to validate against the token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return username.equals(extractedUsername) && !isTokenExpired(token);
        } catch (ExpiredJwtException ex) {
            throw new JwtException("JWT token has expired");
        } catch (SecurityException ex) {
            throw new JwtException("Invalid JWT signature");
        }
    }

    /**
     * Extract the username (subject) from the token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Check if the token is expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the expiration date from the token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from the token.
     *
     * @param <T>           the type of the claim
     * @param token         the JWT token
     * @param claimsResolver a function to extract the claim
     * @return the claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from the token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {

        if (isTokenBlacklisted(token)) {
            throw new RuntimeException("Token is blacklisted");
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
