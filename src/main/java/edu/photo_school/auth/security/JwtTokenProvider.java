package edu.photo_school.auth.security;

import edu.photo_school.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";

    private final JwtProperties jwtProperties;
    private Key key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getSigningKey() {
        if (this.key == null) {
            this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        }
        return this.key;
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_ROLES, userDetails.getAuthorities().stream()
                .map(Object::toString)
                .toList());
        claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS);

        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getAccessTokenValidityMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH);

        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getRefreshTokenValidityMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return TOKEN_TYPE_ACCESS.equals(claims.get(CLAIM_TOKEN_TYPE));
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return TOKEN_TYPE_REFRESH.equals(claims.get(CLAIM_TOKEN_TYPE));
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = parseClaims(token);
        Object roles = claims.get(CLAIM_ROLES);
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream().map(Object::toString).toList();
        }
        return List.of();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
