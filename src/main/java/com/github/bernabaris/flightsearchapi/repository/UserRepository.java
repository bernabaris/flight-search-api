package com.github.bernabaris.flightsearchapi.repository;

import com.github.bernabaris.flightsearchapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("Select user from UserEntity user where user.email = :email")
    UserEntity fetchUserByEmailId(@Param("email") String email);
}
