package com.github.bernabaris.flightsearchapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bernabaris.flightsearchapi.model.AppUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    String authTokenHeader;
    String privateKey;

    protected LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
                          String authTokenHeader, String privateKey) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.authTokenHeader = authTokenHeader;
        this.privateKey = privateKey;
    }

    private final AuthenticationManager authenticationManager;
    private AppUser loginUser;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        setLoginUser(mapper.readValue(request.getInputStream(), AppUser.class));
        log.info("Attempt authentication for user: {}", loginUser.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getEmail(), loginUser.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, authTokenHeader);
        AppUser user = (AppUser) authResult.getPrincipal();
        log.info("Successful authentication for user: {}", user.getEmail());
        response.setHeader(authTokenHeader, TokenUtil.build(user, privateKey));
        SecurityContextHolder.getContext().setAuthentication(authResult);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        mapper.writeValue(response.getWriter(), userMap);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.error(failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public void setLoginUser(AppUser user) {
        this.loginUser = user;
    }
}

