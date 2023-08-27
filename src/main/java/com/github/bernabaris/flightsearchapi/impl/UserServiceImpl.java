package com.github.bernabaris.flightsearchapi.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.bernabaris.flightsearchapi.model.AppUser;
import com.github.bernabaris.flightsearchapi.service.UserService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final DBServiceImpl dbService;

    @Value("${defaults.admin.email:admin@flightbox.com}")
    private String defaultAdminEmail;

    @Value("${defaults.admin.password:admin}")
    private String defaultAdminPassword;

    UserServiceImpl(DBServiceImpl dbService) {
        this.dbService = dbService;
    }

    @PostConstruct
    private void init() {
        if (dbService.getUserByEmail(defaultAdminEmail).isEmpty()) {
            dbService.addOrUpdateUser(AppUser.builder()
                    .email(defaultAdminEmail)
                    .password(hashPassword(defaultAdminPassword))
                    .firstName("admin")
                    .lastName("admin")
                    .admin(true)
                    .active(true)
                    .created(new Date())
                    .build());
            log.info("Default admin user created. email: {}", defaultAdminEmail);
        }
        log.info("{} is started", getClass().getSimpleName());
    }

    @Override
    public String signUp(AppUser user) {
        String status = "User exists. Please Login!";
        Optional<AppUser> existingUser = dbService.getUserByEmail(user.getEmail());

        if (existingUser.isEmpty()) {
            user.setPassword(hashPassword(user.getPassword()));
            user.setAdmin(false);
            user.setActive(true);
            user.setCreated(new Date());
            dbService.addOrUpdateUser(user);
            status = "Success";
        } else {
            log.info(status);
        }
        return status;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Load user: {}", email);
        return fetchUserByEmail(email);
    }

    @Override
    public AppUser fetchUserByEmail(String email) {
        log.info("Fetch user by email: {}", email);
        return dbService.getUserByEmail(email).orElse(null);
    }

    @Override
    public boolean validatePassword(UsernamePasswordAuthenticationToken authentication, AppUser user) {
        log.info("Validate password for user: {}", user.getEmail());
        BCrypt.Result result = BCrypt.verifyer().verify(authentication.getCredentials().toString().toCharArray(),
                user.getPassword());
        return result.verified;
    }
}
