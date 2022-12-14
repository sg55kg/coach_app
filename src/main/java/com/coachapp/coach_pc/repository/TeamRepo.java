package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepo extends JpaRepository<Team, UUID> {
    @Query("select t from Team t where t.coach.id = ?1")
    Optional<List<Team>> findAllByCoachId(UUID coachId);
}
