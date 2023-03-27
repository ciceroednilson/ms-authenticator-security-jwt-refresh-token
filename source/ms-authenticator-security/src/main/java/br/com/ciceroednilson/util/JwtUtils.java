package br.com.ciceroednilson.util;

import br.com.ciceroednilson.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.expiration-token}")
    private int jwtExpirationToken;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String TYPE_BEARER = "Bearer";
    public String generateJwtToken(final Authentication authentication) {
        final UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        return generateTokenFromUsername(userDetailsImpl.getUsername());
    }

    public String findUserNameFromJwtToken(final String authToken) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getSubject();
    }

    public String generateTokenFromUsername(final String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationToken))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (final SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (final MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (final ExpiredJwtException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
        } catch (final UnsupportedJwtException ex) {
            log.error("JWT token is unsupported: {}", ex.getMessage());
        } catch (final IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}
