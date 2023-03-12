package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.coachapp.coach_pc.model.exercise.Exercise;

import java.util.UUID;

@EntityView(Exercise.class)
public interface ExerciseViewModel {
    @IdMapping
    UUID getId();
}
