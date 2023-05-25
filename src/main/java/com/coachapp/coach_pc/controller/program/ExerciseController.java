package com.coachapp.coach_pc.controller.program;

import com.coachapp.coach_pc.model.exercise.AthleteExerciseComment;
import com.coachapp.coach_pc.request.program.AthleteExerciseCommentRequest;
import com.coachapp.coach_pc.request.program.ExerciseRequest;
import com.coachapp.coach_pc.service.program.ExerciseService;
import com.coachapp.coach_pc.view.ExerciseViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/exercise")
public class ExerciseController {

    private ExerciseService exerciseService;
    private Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseViewModel> updateExercise(@PathVariable UUID id, @RequestBody ExerciseRequest exercise) {
        return exerciseService.updateExercise(exercise);
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteExercise(@PathVariable UUID id) {
        logger.info("Received request to delete exercise: " + id);
        return exerciseService.deleteExercise(id);
    }

    @PostMapping("{exerciseId}/comment")
    public ResponseEntity<AthleteExerciseComment> addExerciseComment(@PathVariable UUID exerciseId, @RequestBody AthleteExerciseCommentRequest comment) {
        return exerciseService.addExerciseComment(exerciseId, comment);
    }
}
