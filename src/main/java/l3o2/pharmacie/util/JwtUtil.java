package l3o2.pharmacie.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JWT Utility class
 * @author raphaelcharoze
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    SecretKey currentSecret = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);


    // Generate JWT Token
    public String generateToken(UUID id, Collection<? extends GrantedAuthority> permissions) {
        // 3 h
        long expiration = 10800000;
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(currentSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate and extract username from token
    public String validateTokenAndReturnUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(currentSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List extractRoles(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(currentSecret)
                .build()
                .parseClaimsJws(token)
                .getBody().get("permissions", List.class);
    }
}
