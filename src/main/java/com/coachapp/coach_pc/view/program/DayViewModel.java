package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.WarmUp;
import com.coachapp.coach_pc.model.exercise.Exercise;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@EntityView(Day.class)
public interface DayViewModel {

    @IdMapping
    UUID getId();
    @Mapping("date")
    OffsetDateTime getDate();
    @Mapping("isRestDay")
    boolean getIsRestDay();
    @Mapping(value = "exercises")
    List<Exercise> getExercises();
    @Mapping("warmUp")
    WarmUp getWarmUp();
}
