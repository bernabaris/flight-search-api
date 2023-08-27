package com.github.bernabaris.flightsearchapi.security;

import com.github.bernabaris.flightsearchapi.impl.UserServiceImpl;
import com.github.bernabaris.flightsearchapi.model.AppUser;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public LoginAuthenticationProvider(UserServiceImpl userService) {
        super();
        this.userService = userService;
    }

    private final UserServiceImpl userService;

    @Override
    protected UserDetails retrieveUser(String email, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        log.info("Retrieve user with email: {}", email);
        AppUser user = userService.fetchUserByEmail(email);
        if (user == null) {
            throw new AuthenticationServiceException(String.format("User does not exists: %s", email));
        }
        boolean passwordValid = userService.validatePassword(authentication, user);
        if (!passwordValid) {
            throw new AuthenticationServiceException("Invalid Password...");
        }
        return user;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        log.debug("Additional authentication checks for user: {}", userDetails.getUsername());
    }
}
