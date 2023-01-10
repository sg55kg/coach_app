package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.Exercise;
import com.coachapp.coach_pc.repository.ExerciseRepo;
import com.coachapp.coach_pc.request.ExerciseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExerciseService {

    private ExerciseRepo exerciseRepo;

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public ResponseEntity<Exercise> updateExercise(ExerciseRequest request) {
        boolean exists = exerciseRepo.existsById(request.getId());
        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Exercise exercise = ExerciseRequest.convertRequest(request);
        exercise = exerciseRepo.save(exercise);

        return new ResponseEntity<>(exercise, HttpStatus.OK);

    }

    public ResponseEntity<String> deleteExercise(UUID id) {
        try {
            exerciseRepo.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }
}
