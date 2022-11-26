package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDataRepo extends JpaRepository<UserData, String> {

    @Query("select u from UserData u where u.username = ?1")
    Optional<UserData> findByUsername(String username);
}
