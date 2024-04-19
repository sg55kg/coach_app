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
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)
@Controller
@RequestMapping("/api/exercise")
public class ExerciseController {

    // make instance dependency variables final to avoid accidental overwriting
    private ExerciseService exerciseService;
    private Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // id route param is unnecessary
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseViewModel> updateExercise(@PathVariable UUID id, @RequestBody ExerciseRequest exercise) {
        return exerciseService.updateExercise(exercise);
    }

    // Would keep UUID id or UUID exerciseId naming but be consistent with one or the other for readabilit
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
