package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.exercise.ComplexExercise;
import com.coachapp.coach_pc.model.exercise.DurationExercise;
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
    private List<String> nameArr;
    private List<Integer>  repArr;
    private List<Integer> repCompletedArr;
    private int dropSetPercent;
    private String equipment;
    private EffortIntensity effortIntensity;
    private EffortIntensity actualIntensity;
    private int distanceMeters;
    private int distanceCompletedMeters;
    private int secondsPerSet;
    private int secondsPerSetCompleted;

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

    public List<String> getNameArr() {
        return nameArr;
    }

    public void setNameArr(List<String> nameArr) {
        this.nameArr = nameArr;
    }

    public List<Integer> getRepArr() {
        return repArr;
    }

    public void setRepArr(List<Integer> repArr) {
        this.repArr = repArr;
    }

    public List<Integer> getRepCompletedArr() {
        return repCompletedArr;
    }

    public void setRepCompletedArr(List<Integer> repCompletedArr) {
        this.repCompletedArr = repCompletedArr;
    }

    public int getDropSetPercent() {
        return dropSetPercent;
    }

    public void setDropSetPercent(int dropSetPercent) {
        this.dropSetPercent = dropSetPercent;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public EffortIntensity getEffortIntensity() {
        return effortIntensity;
    }

    public void setEffortIntensity(EffortIntensity effortIntensity) {
        this.effortIntensity = effortIntensity;
    }

    public EffortIntensity getActualIntensity() {
        return actualIntensity;
    }

    public void setActualIntensity(EffortIntensity actualIntensity) {
        this.actualIntensity = actualIntensity;
    }

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public int getDistanceCompletedMeters() {
        return distanceCompletedMeters;
    }

    public void setDistanceCompletedMeters(int distanceCompletedMeters) {
        this.distanceCompletedMeters = distanceCompletedMeters;
    }

    public int getSecondsPerSet() {
        return secondsPerSet;
    }

    public void setSecondsPerSet(int secondsPerSet) {
        this.secondsPerSet = secondsPerSet;
    }

    public int getSecondsPerSetCompleted() {
        return secondsPerSetCompleted;
    }

    public void setSecondsPerSetCompleted(int secondsPerSetCompleted) {
        this.secondsPerSetCompleted = secondsPerSetCompleted;
    }

    public static Exercise convertRequest(Exercise exercise, ExerciseRequest request) {
        if (exercise == null) {
            if(request.getType() == ExerciseType.EXERCISE) {
                exercise = new Exercise();
            } else if (request.getType() == ExerciseType.COMPLEX) {
                exercise = new ComplexExercise();
            } else if (request.getType() == ExerciseType.DURATION) {
                exercise = new DurationExercise();
            }

        }

        for (AthleteExerciseCommentRequest commentRequest : request.getComments()) {
            AthleteExerciseComment comment = AthleteExerciseCommentRequest.convertRequest(commentRequest);
            comment.setExercise(exercise);
            exercise.getComments().add(comment);
        }

        exercise.setId(request.getId());
        exercise.setWeight(request.getWeight());
        exercise.setSets(request.getSets());
        exercise.setWeightCompleted(request.getWeightCompleted());
        exercise.setIsMax(request.getIsMax());
        exercise.setNotes(request.getNotes());
        exercise.setIsComplete(request.getIsComplete());
        exercise.setOrder(request.getOrder());
        exercise.setSetsCompleted(request.getSetsCompleted());

        if (request.getType() == ExerciseType.EXERCISE) {
            exercise.setName(request.getName());
            exercise.setRepsPerSet(request.getRepsPerSet());
            exercise.setTotalRepsCompleted(request.getTotalRepsCompleted());
        } else if (request.getType() == ExerciseType.COMPLEX) {
            ((ComplexExercise) exercise).setRepArr(
                    ComplexExercise.convertRepArrListToString(request.getRepArr())
            );
            ((ComplexExercise) exercise).setNameArr(
                    ComplexExercise.convertNameArrListToString(request.getNameArr())
            );
            ((ComplexExercise) exercise).setRepCompletedArr(
                    ComplexExercise.convertRepCompletedArrListToString(request.getRepCompletedArr())
            );
        } else if (request.getType() == ExerciseType.DURATION) {
            ((DurationExercise) exercise).setEffortIntensity(request.getEffortIntensity());
            ((DurationExercise) exercise).setActualIntensity(request.getActualIntensity());
            ((DurationExercise) exercise).setEquipment(request.getEquipment());
            ((DurationExercise) exercise).setDistanceMeters(request.getDistanceMeters());
            ((DurationExercise) exercise).setDistanceCompletedMeters(request.getDistanceCompletedMeters());
            ((DurationExercise) exercise).setSecondsPerSet(request.getSecondsPerSet());
            ((DurationExercise) exercise).setSecondsPerSetCompleted(request.getSecondsPerSetCompleted());
        }


        if (request.getDropSets() != null && request.getDropSets().size() > 0) {
            for (ExerciseRequest r : request.getDropSets()) {
                Exercise dropSet = ExerciseRequest.convertRequest(null, r);
                dropSet.setDropSetPercent(r.getDropSetPercent());
                if (exercise.getWeightCompleted() > 0 && dropSet.getWeightCompleted() < 1) {
                    int percentOfTopSetWeight =
                            (int)Math.round((dropSet.getDropSetPercent() / 100.0) * exercise.getWeightCompleted());
                    dropSet.setWeightCompleted(percentOfTopSetWeight);
                    dropSet.setIsComplete(true);
                }
                //if (r.getId() == null) {
                    exercise.addDropSet(dropSet);
                //}
                dropSet.setTopSet(exercise);
            }
        }

        return exercise;
    }
}
