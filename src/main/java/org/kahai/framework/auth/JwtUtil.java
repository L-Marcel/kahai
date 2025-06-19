package org.kahai.framework.auth;

import java.util.Date;
import java.util.UUID;

import org.kahai.framework.KahaiProperties;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
    private final Long expiration;
    private final Algorithm algorithm;

    public JwtUtil(KahaiProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.getJwt().getSecret());
        this.expiration = properties.getJwt().getExpiration();
    };

    public String generateToken(UUID uuid) {
        Date currentDate = new Date();
        Date expirationDate = new Date(
            currentDate.getTime() + this.expiration
        );

        return JWT
            .create()
            .withSubject(uuid.toString())
            .withIssuedAt(currentDate)
            .withExpiresAt(expirationDate)
            .sign(this.algorithm);
    };

    public UUID validateTokenAndGetUserId(String token) {
        JWTVerifier verifier = JWT.require(this.algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String userId = decodedJWT.getSubject();

        return UUID.fromString(userId);
    };
};
