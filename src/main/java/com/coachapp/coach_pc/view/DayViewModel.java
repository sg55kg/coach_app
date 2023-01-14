package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.WarmUp;
import com.coachapp.coach_pc.model.exercise.Exercise;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DayViewModel {

    private UUID id;
    private OffsetDateTime date;
    private List<Exercise> exercises;
    private boolean isRestDay;
    private WarmUp warmUp;

    public DayViewModel() {}

    public DayViewModel(UUID id, OffsetDateTime date, List<Exercise> exercises, boolean isRestDay, WarmUp warmUp) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
        this.isRestDay = isRestDay;
        this.warmUp = warmUp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public boolean getIsRestDay() {
        return isRestDay;
    }

    public void setIsRestDay(boolean isRestDay) {
        this.isRestDay = isRestDay;
    }

    public WarmUp getWarmUp() {
        return warmUp;
    }

    public void setWarmUp(WarmUp warmUp) {
        this.warmUp = warmUp;
    }
}
