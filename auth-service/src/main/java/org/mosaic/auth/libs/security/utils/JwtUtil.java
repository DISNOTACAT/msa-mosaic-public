package org.mosaic.auth.libs.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.mosaic.auth.domain.model.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {
    private SecretKey secretKey; // 'final' 제거
    @Value("${spring.jwt.secret}")
    private String jwtSecret;
    private static final String USERUUID_CLAIM_KEY = "userUuid";
    private Long accessTokenExpirationPeriod = 60 * 30 * 1000L;


    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }


    public String getUserUuid(String token) {
        return parseToken(token).get(USERUUID_CLAIM_KEY, String.class);
    }


    public UserRole getRole(String token) {
        return UserRole.valueOf(parseToken(token).get("role", String.class));
    }

    public Boolean isExpired(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        if (!validateToken(token)) {
            throw new IllegalArgumentException("Token is not valid");
        }

        return parseToken(token).getExpiration().before(new Date());
    }

    private String createToken(String userUuid, String role, Date expirationDate) {
        return Jwts.builder()
                .claim(USERUUID_CLAIM_KEY, userUuid)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createAccessToken(String userUuid, String role) {

        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(accessTokenExpirationPeriod);
        Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return createToken(userUuid, role, expirationDate);
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT token", ex);
            return false;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
            return false;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("Empty JWT token", ex);
            return false;
        } catch (JwtException ex) {
            log.error("Failed to validate JWT token", ex);
            return false;
        }
    }
}
