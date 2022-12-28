package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.Exercise;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DayViewModel {

    private UUID id;
    private Date date;
    private List<Exercise> exercises;
    private boolean isRestDay;

    public DayViewModel() {}

    public DayViewModel(UUID id, Date date, List<Exercise> exercises, boolean isRestDay) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
        this.isRestDay = isRestDay;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
        isRestDay = isRestDay;
    }
}
