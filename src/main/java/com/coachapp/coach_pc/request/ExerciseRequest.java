package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.exercise.ComplexExercise;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private int setsCompleted;
    private boolean isMax;
    private WeightIntensity weightIntensity;
    private List<AthleteExerciseCommentRequest> comments;
    private boolean isComplete;
    private UUID dayId;
    private UUID programId;
    private String notes;
    private int order;
    private List<ExerciseRequest> dropSets;
    private ExerciseType type;

    public ExerciseRequest() {}


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
    @JsonProperty(value="isMax")
    public boolean getIsMax() {
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

    public int getSetsCompleted() {
        return setsCompleted;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWeightCompleted(int weightCompleted) {
        this.weightCompleted = weightCompleted;
    }

    public void setTotalRepsCompleted(int totalRepsCompleted) {
        this.totalRepsCompleted = totalRepsCompleted;
    }

    public void setSetsCompleted(int setsCompleted) {
        this.setsCompleted = setsCompleted;
    }

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }

    public void setWeightIntensity(WeightIntensity weightIntensity) {
        this.weightIntensity = weightIntensity;
    }

    public void setComments(List<AthleteExerciseCommentRequest> comments) {
        this.comments = comments;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void setDayId(UUID dayId) {
        this.dayId = dayId;
    }

    public void setProgramId(UUID programId) {
        this.programId = programId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<ExerciseRequest> getDropSets() {
        return dropSets;
    }

    public void setDropSets(List<ExerciseRequest> dropSets) {
        this.dropSets = dropSets;
    }

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public static Exercise convertRequest(Exercise exercise, ExerciseRequest request) {
        if (exercise == null) {
            if(request.getType() == ExerciseType.EXERCISE) {
                exercise = new Exercise();
            } else if (request.getType() == ExerciseType.COMPLEX) {
                exercise = new ComplexExercise();
            }

        }

        for (AthleteExerciseCommentRequest commentRequest : request.getComments()) {
            AthleteExerciseComment comment = AthleteExerciseCommentRequest.convertRequest(commentRequest);
            comment.setExercise(exercise);
            exercise.getComments().add(comment);
        }

        if (request.getType() == ExerciseType.EXERCISE) {
            exercise.setWeight(request.getWeight());
            exercise.setName(request.getName());
            exercise.setSets(request.getSets());
            exercise.setRepsPerSet(request.getRepsPerSet());
            exercise.setWeightCompleted(request.getWeightCompleted());
            exercise.setTotalRepsCompleted(request.getTotalRepsCompleted());
            exercise.setIsMax(request.getIsMax());
            exercise.setNotes(request.getNotes());
            exercise.setIsComplete(request.getIsComplete());
            exercise.setOrder(request.getOrder());
            exercise.setSetsCompleted(request.getSetsCompleted());
        } else if (request.getType() == ExerciseType.COMPLEX) {
            // TODO
        }


        if (request.getDropSets() != null && request.getDropSets().size() > 0) {
            for (ExerciseRequest r : request.getDropSets()) {
                Exercise dropSet = ExerciseRequest.convertRequest(null, r);
                if (r.getId() == null) {
                    exercise.addDropSet(dropSet);
                }
                dropSet.setTopSet(exercise);
            }
        }

        return exercise;
    }
}
