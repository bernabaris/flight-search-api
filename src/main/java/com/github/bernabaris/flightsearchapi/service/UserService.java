package com.github.bernabaris.flightsearchapi.service;

import com.github.bernabaris.flightsearchapi.model.AppUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface UserService {
    String signUp(AppUser user);

    AppUser fetchUserByEmail(String email);

    boolean validatePassword(UsernamePasswordAuthenticationToken authentication, AppUser user);
}
