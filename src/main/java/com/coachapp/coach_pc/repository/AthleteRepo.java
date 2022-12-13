package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.AthleteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AthleteRepo extends JpaRepository<AthleteData, UUID> {
}
