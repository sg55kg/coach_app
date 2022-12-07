package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Exercise;
import com.coachapp.coach_pc.model.Program;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    public static Day convertRequest(DayRequest dayRequest, Program program) {
        Day day = new Day();
        day.setDate(dayRequest.getDate());
        day.setProgram(program);
        for (Exercise exercise : dayRequest.getExercises()) {
            exercise.setDay(day);
        }
        day.setExercises(dayRequest.getExercises());

        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayRequest that = (DayRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
