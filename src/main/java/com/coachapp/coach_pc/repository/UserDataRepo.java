package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserDataRepo extends JpaRepository<UserData, UUID> {

    @Query("select u from UserData u where u.username = ?1")
    Optional<UserData> findByUsername(String username);

    @Query("select u from UserData u where u.email = ?1")
    Optional<UserData> findByEmail(String email);
}
