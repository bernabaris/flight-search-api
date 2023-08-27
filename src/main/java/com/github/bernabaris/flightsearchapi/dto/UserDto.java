package com.github.bernabaris.flightsearchapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean admin;
    private boolean active;
    private Date created;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='*****" + '\'' +
                ", admin=" + admin +
                ", active=" + active +
                ", created=" + created +
                '}';
    }
}
