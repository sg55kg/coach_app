package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramRepo extends JpaRepository<Program, UUID> {
}
