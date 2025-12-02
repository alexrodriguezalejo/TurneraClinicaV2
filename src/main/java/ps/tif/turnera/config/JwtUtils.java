package ps.tif.turnera.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Clave secreta para firmar el token (debe ser larga y segura, idealmente en properties)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración (ej: 24 horas)
    private final long expirationMillis = 24 * 60 * 60 * 1000;

    // Genera token con el username (correo) y roles
    public String generarToken(String username) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(key)
                .compact();
    }

    // Extrae username del token
    public String obtenerUsernameDelToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida token (firma y expiración)
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido o expirado
            return false;
        }
    }
}
