package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Exercise;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DayRequest {

    private UUID id;
    private Date date;
    private List<Exercise> exercises;

    public DayRequest() {}

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
}
