package com.github.bernabaris.flightsearchapi.controller;

import com.github.bernabaris.flightsearchapi.dto.DtoUtils;
import com.github.bernabaris.flightsearchapi.dto.UserDto;
import com.github.bernabaris.flightsearchapi.dto.UserInputDto;
import com.github.bernabaris.flightsearchapi.impl.UserServiceImpl;
import com.github.bernabaris.flightsearchapi.model.AppUser;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public String login() {
        return "Success";
    }

    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable("email") String email, HttpServletResponse response) throws IOException {
        AppUser user = userService.fetchUserByEmail(email);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("User not found: %s", email));
            return null;
        }
        return DtoUtils.convertToUserDto(user);
    }

    @PostMapping
    public void signUp(@RequestBody UserInputDto userInputDto, HttpServletResponse response) {
        try {
            log.info("Sign up user: {}", userInputDto);
            String status = userService.signUp(DtoUtils.convertToUser(userInputDto));
            if (!"Success".equalsIgnoreCase(status)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, status);
            } else {
                response.setStatus(200);
            }
        } catch (Exception e) {
            log.error("Exception occurred while sign up. user: {}", userInputDto, e);
        }
    }
}
