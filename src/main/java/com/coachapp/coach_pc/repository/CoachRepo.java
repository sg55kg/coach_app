package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.user.CoachData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoachRepo extends JpaRepository<CoachData, UUID> {

}
