package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.Exercise;

import java.util.List;
import java.util.UUID;

public class ExerciseRequest {

    private UUID id;
    private String name;
    private int sets;
    private int repsPerSet;
    private int weight;
    private int weightCompleted;
    private int totalRepsCompleted;
    private boolean isMax;
    private WeightIntensity weightIntensity;
    private List<AthleteExerciseComment> comments;

    public ExerciseRequest() {}

    public ExerciseRequest(
            UUID id,
            String name,
            int sets,
            int repsPerSet,
            int weight,
            int weightCompleted,
            int totalRepsCompleted,
            boolean isMax,
            WeightIntensity weightIntensity,
            List<AthleteExerciseComment> comments
    ) {
        this.id = id;
        this.name = name;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.weight = weight;
        this.weightCompleted = weightCompleted;
        this.totalRepsCompleted = totalRepsCompleted;
        this.isMax = isMax;
        this.weightIntensity = weightIntensity;
        this.comments = comments;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public int getWeight() {
        return weight;
    }

    public int getWeightCompleted() {
        return weightCompleted;
    }

    public int getTotalRepsCompleted() {
        return totalRepsCompleted;
    }

    public boolean isMax() {
        return isMax;
    }

    public WeightIntensity getWeightIntensity() {
        return weightIntensity;
    }

    public List<AthleteExerciseComment> getComments() {
        return comments;
    }

    public static Exercise convertRequest(ExerciseRequest request) {
        Exercise exercise = new Exercise();
        request.getComments().forEach(c -> {
            c.setExercise(exercise);
        });

        exercise.setId(request.getId());
        exercise.setName(request.getName());
        exercise.setSets(request.getSets());
        exercise.setRepsPerSet(request.getRepsPerSet());
        exercise.setWeightCompleted(request.getWeightCompleted());
        exercise.setTotalRepsCompleted(request.getTotalRepsCompleted());
        exercise.setWeightIntensity(request.getWeightIntensity());
        exercise.setIsMax(request.isMax());
        exercise.setComments(request.getComments());

        return exercise;
    }
}
