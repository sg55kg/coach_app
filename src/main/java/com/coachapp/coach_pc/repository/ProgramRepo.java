package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgramRepo extends JpaRepository<Program, UUID> {

    @Query("select p from Program p where p.coach.id = ?1")
    List<Program> getProgramsByCoachId(UUID coachId);

    @Query("select p from Program p where p.team.id = ?1")
    List<Program> getProgramsByTeamId(UUID teamId);
}
