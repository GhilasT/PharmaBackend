package l3o2.pharmacie.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import l3o2.pharmacie.api.model.entity.Employe;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour la gestion des JSON Web Tokens (JWT).
 * Fournit des méthodes pour générer, valider et extraire des informations des tokens.
 * @author raphaelcharoze
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // Utilise une clé secrète unique pour toutes les opérations
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final int TOKEN_VALIDITY = 3600 * 5; // 5 heures

    /**
     * Génère un token JWT.
     * @param id L'identifiant de l'utilisateur (sujet du token).
     * @param permissions La collection des autorisations de l'utilisateur.
     * @return Le token JWT généré sous forme de chaîne.
     */
    public String generateToken(UUID id, Collection<? extends GrantedAuthority> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valide un token JWT et retourne le nom d'utilisateur (sujet).
     * @param token Le token JWT à valider.
     * @return Le nom d'utilisateur extrait du token.
     * @throws io.jsonwebtoken.JwtException si le token est invalide ou a expiré.
     */
    public String validateTokenAndReturnUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extrait les rôles (permissions) d'un token JWT.
     * @param token Le token JWT.
     * @return Une liste des rôles contenus dans le token.
     */
    public List extractRoles(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().get("permissions", List.class);
    }

    /**
     * Extrait le nom d'utilisateur (sujet) d'un token JWT.
     * @param token Le token JWT.
     * @return Le nom d'utilisateur.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration d'un token JWT.
     * @param token Le token JWT.
     * @return La date d'expiration.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait une revendication (claim) spécifique d'un token JWT à l'aide d'une fonction de résolution.
     * @param token Le token JWT.
     * @param claimsResolver La fonction pour extraire la revendication.
     * @param <T> Le type de la revendication.
     * @return La valeur de la revendication.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait toutes les revendications (claims) d'un token JWT.
     * @param token Le token JWT.
     * @return L'objet Claims contenant toutes les revendications.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si un token JWT a expiré.
     * @param token Le token JWT.
     * @return {@code true} si le token a expiré, {@code false} sinon.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Génère un token JWT à partir des détails d'un utilisateur (UserDetails).
     * Les rôles de l'utilisateur sont inclus dans les revendications du token.
     * @param userDetails Les détails de l'utilisateur.
     * @return Le token JWT généré.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Génère un token JWT pour un employé.
     * Les rôles de l'employé sont inclus dans les revendications du token.
     * @param employe L'objet Employe.
     * @return Le token JWT généré.
     */
    public String generateToken(Employe employe) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = employe.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return createToken(claims, employe.getUsername());
    }

    /**
     * Génère un token JWT avec un sujet et une collection de rôles spécifiés.
     * @param subject Le sujet du token (généralement le nom d'utilisateur ou l'ID).
     * @param roles La collection des rôles à inclure dans le token.
     * @return Le token JWT généré.
     */
    public String generateToken(String subject, Collection<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return createToken(claims, subject);
    }

    /**
     * Crée un token JWT avec les revendications et le sujet donnés.
     * @param claims La map des revendications à inclure dans le token.
     * @param subject Le sujet du token.
     * @return Le token JWT généré.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valide un token JWT par rapport aux détails d'un utilisateur.
     * Vérifie si le nom d'utilisateur dans le token correspond et si le token n'a pas expiré.
     * @param token Le token JWT.
     * @param userDetails Les détails de l'utilisateur.
     * @return {@code true} si le token est valide, {@code false} sinon.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Génère un token JWT avec l'email donné comme sujet.
     * @param email L'email à utiliser comme sujet.
     * @return Le token JWT.
     */
    public String generateTokenFromEmail(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
