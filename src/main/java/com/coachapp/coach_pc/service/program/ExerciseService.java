package com.coachapp.coach_pc.service.program;

import com.coachapp.coach_pc.model.exercise.AthleteExerciseComment;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.repository.program.ExerciseRepo;
import com.coachapp.coach_pc.request.program.AthleteExerciseCommentRequest;
import com.coachapp.coach_pc.request.program.ExerciseRequest;
import com.coachapp.coach_pc.view.ExerciseViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// Add logs to every service method indicating request params (masking private data) and response values for debugging
@Service
public class ExerciseService {

    private ExerciseRepo exerciseRepo;
    private Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    public ExerciseService(ExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    public ResponseEntity<ExerciseViewModel> updateExercise(ExerciseRequest request) {
        Optional<? extends Exercise> optional;

        // use functional optional.map().orElseGet() for readability and maintainability.
        // in general you always want to assume you have access to the variables in the current scope, which
        // the map function provides
        optional = exerciseRepo.findById(request.getId());

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        var exercise = optional.get();
        exercise = ExerciseRequest.convertRequest(exercise, request);
        exercise = exerciseRepo.save(exercise);
        ExerciseViewModel vm = ExerciseViewModel.convertExercise(exercise);
        return new ResponseEntity<>(vm, HttpStatus.OK);

    }

    public ResponseEntity<String> deleteExercise(UUID id) {
        try {
            exerciseRepo.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.trace(e.getStackTrace().toString());
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    public ResponseEntity<AthleteExerciseComment> addExerciseComment(UUID exerciseId, AthleteExerciseCommentRequest request) {
        Optional<Exercise> optional = exerciseRepo.findById(exerciseId);

        // use functional optional.map().orElseGet() for readability and maintainability.
        // in general you always want to assume you have access to the variables in the current scope, which
        // the map function provides
        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteExerciseComment comment = AthleteExerciseCommentRequest.convertRequest(request);
        Exercise exercise = optional.get();
        comment.setExercise(exercise);
        exercise.addComment(comment);
        exercise = exerciseRepo.save(exercise);

        int size = exercise.getComments().size();
        comment = exercise.getComments().get(size - 1);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
}
