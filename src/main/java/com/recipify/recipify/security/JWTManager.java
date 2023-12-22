package com.recipify.recipify.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTManager {

    /**
     * JWT expiration duration set as 60mins, which means token is not valid after 60mins.
     */
    private static final long JWT_EXPIRATION_DURATION =  60 * 60 * 1000;
    private static final String JWT_SECRET = "imrandomsecretandyoushouldchangeme";
    private static final String BEARER_HEADER = "Bearer";

    /**
     * Used to generate new JWT.
     *
     * @param authentication Spring security Authentication principal
     * @return JWT
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION_DURATION);

        return JWT.create()
                .withSubject(username)
                .withIssuer("Recipify API")
                .withIssuedAt(currentDate)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    /**
     * Used to validate JWT.
     *
     * @param token JWT
     * @return {@link DecodedJWT}
     */
    public DecodedJWT decodeAndValidateToken(String token) {
        if (token == null) throw new RuntimeException("Authorization token is missing");

        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Extracts token from HTTP authorization header.
     *
     * @param request {@link HttpServletRequest}
     * @return JWT or null if missing
     */
    public String getTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!ObjectUtils.isEmpty(authorizationHeader)) {

            if (!authorizationHeader.startsWith(BEARER_HEADER)) {
                throw new RuntimeException("Invalid authorization header");
            }

            return authorizationHeader.substring(BEARER_HEADER.length() + 1);
        }

        return null;
    }

}
