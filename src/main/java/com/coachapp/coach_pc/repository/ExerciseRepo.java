package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise, UUID> {
}
