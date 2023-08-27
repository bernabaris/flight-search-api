package com.github.bernabaris.flightsearchapi.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Value("${auth-token-header}")
    private String authTokenHeader;

    @Value("${privateKey}")
    private String privateKey;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(authTokenHeader);
        if (header == null || header.equals("undefined")) {
            chain.doFilter(req, res);
            log.debug("{} header is not found for method:{} path:{}", authTokenHeader,
                    req.getMethod(), req.getServletPath());
            return;
        }
        try {
            DecodedJWT decodedJWT = TokenUtil.decode(header, privateKey);
            log.debug("User \"{}\" is authorized for req method:{} path:{}",
                    decodedJWT.getClaim(TokenUtil.EMAIL_CLAIM).asString(),
                    req.getMethod(), req.getServletPath());
            chain.doFilter(req, res);
        } catch (TokenExpiredException e) {
            log.debug("{}", e.getMessage());
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired!");
        } catch (Exception e) {
            log.error("Exception occurred while decoding auth token: {}", e.getMessage());
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not valid!");
        }
    }
}
