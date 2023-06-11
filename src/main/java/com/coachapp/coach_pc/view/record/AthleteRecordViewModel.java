package com.coachapp.coach_pc.view.record;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.user.AthleteRecord;

import java.time.OffsetDateTime;
import java.util.UUID;


@EntityView(AthleteRecord.class)
public interface AthleteRecordViewModel {
    @IdMapping
    UUID getId();
    @Mapping("athlete.id")
    UUID getAthleteId();
    @Mapping("exercise.id")
    UUID getExerciseId();
    @Mapping("day.id")
    UUID getDayId();
    int getNumReps();
    int getWeight();
    OffsetDateTime getCreatedAt();
    String getExerciseName();
    @Mapping("isCurrent")
    boolean getIsCurrent();
    void setIsCurrent(boolean isCurrent);
}
