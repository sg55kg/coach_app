package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Exercise;
import com.coachapp.coach_pc.model.Program;

import java.util.ArrayList;
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
    private List<AthleteExerciseCommentRequest> comments;
    private boolean isComplete;
    private UUID dayId;
    private UUID programId;
    private String notes;
    private int order;

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
            List<AthleteExerciseCommentRequest> comments,
            UUID dayId,
            UUID programId,
            boolean isComplete,
            String notes,
            int order
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
        this.isComplete = isComplete;
        this.dayId = dayId;
        this.programId = programId;
        this.notes = notes;
        this.order = order;
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

    public List<AthleteExerciseCommentRequest> getComments() {
        return comments;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public UUID getDayId() {
        return dayId;
    }

    public UUID getProgramId() {
        return programId;
    }

    public String getNotes() {
        return notes;
    }

    public int getOrder() {
        return order;
    }

    public static Exercise convertRequest(ExerciseRequest request) {
        Exercise exercise = new Exercise();
        List<AthleteExerciseComment> comments = new ArrayList<>();


        request.getComments().forEach(c -> {
            comments.add(AthleteExerciseCommentRequest.convertRequest(c));
        });

        exercise.setId(request.getId());
        exercise.setWeight(request.getWeight());
        exercise.setName(request.getName());
        exercise.setSets(request.getSets());
        exercise.setRepsPerSet(request.getRepsPerSet());
        exercise.setWeightCompleted(request.getWeightCompleted());
        exercise.setTotalRepsCompleted(request.getTotalRepsCompleted());
        exercise.setWeightIntensity(request.getWeightIntensity());
        exercise.setIsMax(request.isMax());
        exercise.setComments(comments);
        exercise.setNotes(request.getNotes());
        exercise.setIsComplete(request.getIsComplete());
        exercise.setOrder(request.getOrder());

        return exercise;
    }
}
