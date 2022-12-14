package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.Exercise;
import com.coachapp.coach_pc.request.ExerciseRequest;
import com.coachapp.coach_pc.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/exercise")
public class ExerciseController {

    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PutMapping("{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable UUID id, @RequestBody ExerciseRequest exercise) {
        return exerciseService.updateExercise(exercise);
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteExercise(@PathVariable UUID id) {
        return exerciseService.deleteExercise(id);
    }
}
