package com.coachapp.coach_pc.request.program;

import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.WarmUp;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.model.program.Program;

import java.time.OffsetDateTime;
import java.util.*;

public class DayRequest {

    private UUID id;
    private OffsetDateTime date;
    private List<ExerciseRequest> exercises;
    private boolean isRestDay;
    private WarmUp warmUp;

    public DayRequest() {}

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

    public List<ExerciseRequest> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseRequest> exercises) {
        this.exercises = exercises;
    }

    public void setIsRestDay(boolean isRestDay) {
        this.isRestDay = isRestDay;
    }

    public boolean getIsRestDay() {
        return isRestDay;
    }

    public WarmUp getWarmUp() {
        return warmUp;
    }

    public void setWarmUp(WarmUp warmUp) {
        this.warmUp = warmUp;
    }

    public static Day convertRequest(DayRequest dayRequest, Program program) {
        Day day = new Day();
        day.setDate(dayRequest.getDate());
        day.setProgram(program);
        day.setId(dayRequest.getId());
        day.setIsRestDay(dayRequest.getIsRestDay());
        if (dayRequest.getWarmUp() != null) {
            dayRequest.getWarmUp().setDay(day);
            day.setWarmUp(dayRequest.getWarmUp());
        }

        List<Exercise> exercises = new ArrayList<>();
        for (ExerciseRequest r : dayRequest.getExercises()) {
            Exercise exercise = ExerciseRequest.convertRequest(null, r);
            exercise.setDay(day);
            exercises.add(exercise);
            if (exercise.getDropSets() != null && exercise.getDropSets().size() > 0) {
                exercise.getDropSets().forEach(ds -> {
                    ds.setTopSet(exercise);
                });
            }
        }
        day.setExercises(exercises);
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
