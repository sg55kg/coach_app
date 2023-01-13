package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.repository.ExerciseRepo;
import com.coachapp.coach_pc.request.ExerciseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseService {

    private ExerciseRepo exerciseRepo;

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public ResponseEntity<Exercise> updateExercise(ExerciseRequest request) {
        Optional<Exercise> optional = exerciseRepo.findById(request.getId());
        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Exercise exercise = optional.get();
        exercise = ExerciseRequest.convertRequest(exercise, request);
        exercise = exerciseRepo.save(exercise);

        return new ResponseEntity<>(exercise, HttpStatus.OK);

    }

    public ResponseEntity<String> deleteExercise(UUID id) {
        try {
            exerciseRepo.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }
}
