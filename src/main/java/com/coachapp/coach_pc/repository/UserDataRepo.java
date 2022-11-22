package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepo extends JpaRepository<UserData, String> {
}
