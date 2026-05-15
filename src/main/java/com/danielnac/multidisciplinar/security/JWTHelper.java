package com.danielnac.multidisciplinar.security;

import com.danielnac.multidisciplinar.dto.TokenDTO;
import com.danielnac.multidisciplinar.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTHelper {

    public static Integer EXPIRES_IN_MINUTES = 60;

    private static final String JWT_SECRET = "2f6c5d2f6d5a5011f9f498476cf424b57b6ad5f90cf225c03525de77b78ed45ec57f6d9ebddaa20a130f555cb871f00bbefd5d3fea501b24ca5d5578122acfd2";

    private static final String JWT_ISSUER = "raizes-nordeste-auth";

    public static TokenDTO createJwtToken(Usuario usuario) {
        Date expiraEm = Date.from(LocalDateTime.now().plusMinutes(EXPIRES_IN_MINUTES).atZone(ZoneId.systemDefault()).toInstant());

        Map<String, Object> dados = new HashMap<>();
        dados.put("id", usuario.getId());
        dados.put("email", usuario.getEmail());
        dados.put("nome", usuario.getNome());
        dados.put("cargo", usuario.getCargo().name());
        dados.put("exp", expiraEm.getTime() / 1000);

        JwtBuilder builder = Jwts.builder()
                .claims(dados)
                .issuer(JWT_ISSUER)
                .expiration(expiraEm)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET)));

        String jwtToken = builder.compact();

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(jwtToken);
        tokenDTO.setDataExpiracao(expiraEm.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        tokenDTO.setId(usuario.getId());
        tokenDTO.setEmail(usuario.getEmail());
        tokenDTO.setNome(usuario.getNome());
        tokenDTO.setCargo(usuario.getCargo().name());

        return tokenDTO;
    }

    public static TokenDTO readJwtToken(String jwtToken) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET)))
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setId(((Number) claims.get("id")).intValue());
        tokenDTO.setEmail(claims.get("email", String.class));
        tokenDTO.setNome(claims.get("nome", String.class));
        tokenDTO.setCargo(claims.get("cargo", String.class));
        tokenDTO.setDataExpiracao(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        return tokenDTO;
    }
}
