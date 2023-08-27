package com.github.bernabaris.flightsearchapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInputDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserInputDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='*****" +
                '}';
    }
}
