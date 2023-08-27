package com.github.bernabaris.flightsearchapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.github.bernabaris.flightsearchapi.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TokenUtil {

    private TokenUtil() {
    }

    private static final int TOKEN_VALID_DURATION_MIN = 60 * 24;
    public static final String USER_ID_CLAIM = "id";
    public static final String EMAIL_CLAIM = "email";

    public static String build(AppUser authUser, String privateKey) {
        log.debug("Build auth token for user: {}", authUser.getEmail());
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            token = JWT.create().withClaim(USER_ID_CLAIM, authUser.getId()).withClaim(EMAIL_CLAIM, authUser.getEmail())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALID_DURATION_MIN * 60 * 1000))
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("Error occurred while creating token for user: {}", authUser.getEmail(), e);
        }
        return token;
    }

    public static DecodedJWT decode(String jwt, String privateKey) throws Exception {
        log.debug("Decode auth token.");
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwt);
    }
}
