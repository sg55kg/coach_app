package com.coachapp.coach_pc.request.program;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.exercise.AthleteExerciseComment;
import com.coachapp.coach_pc.model.exercise.AccessoryExercise;
import com.coachapp.coach_pc.model.exercise.ComplexExercise;
import com.coachapp.coach_pc.model.exercise.DurationExercise;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class ExerciseRequest {

    @JsonProperty(value = "id")
    public UUID id;
    @JsonProperty(value = "name")
    public String name;
    @JsonProperty(value = "sets")
    public int sets;
    @JsonProperty(value = "repsPerSet")
    public int repsPerSet;
    @JsonProperty(value = "weight")
    public int weight;
    @JsonProperty(value = "weightCompleted")
    public int weightCompleted;
    @JsonProperty(value = "totalRepsCompleted")
    public int totalRepsCompleted;
    @JsonProperty(value = "setsCompleted")
    public int setsCompleted;
    @JsonProperty(value = "isMax")
    public boolean isMax;
    @JsonProperty(value = "weightIntensity")
    public WeightIntensity weightIntensity;
    @JsonProperty(value = "comments")
    public List<AthleteExerciseCommentRequest> comments;
    @JsonProperty(value = "isComplete")
    public boolean isComplete;
    @JsonProperty(value = "dayId")
    public UUID dayId;
    @JsonProperty(value = "programId")
    public UUID programId;
    @JsonProperty(value = "notes")
    public String notes;
    @JsonProperty(value = "order")
    public int order;
    @JsonProperty(value = "dropSets")
    public List<ExerciseRequest> dropSets;
    @JsonProperty(value = "type")
    public ExerciseType type;
    @JsonProperty(value = "nameArr")
    public List<String> nameArr;
    @JsonProperty(value = "repArr")
    public List<Integer>  repArr;
    @JsonProperty(value = "repCompletedArr")
    public List<Integer> repCompletedArr;
    @JsonProperty(value = "dropSetPercent")
    public int dropSetPercent;
    @JsonProperty(value = "equipment")
    public String equipment;
    @JsonProperty(value = "effortIntensity")
    public EffortIntensity effortIntensity;
    @JsonProperty(value = "actualIntensity")
    public EffortIntensity actualIntensity;
    @JsonProperty(value = "distanceMeters")
    public int distanceMeters;
    @JsonProperty(value = "distanceCompletedMeters")
    public int distanceCompletedMeters;
    @JsonProperty(value = "secondsPerSet")
    public int secondsPerSet;
    @JsonProperty(value = "secondsPerSetCompleted")
    public int secondsPerSetCompleted;
    @JsonProperty(value = "isMaxReps")
    public boolean isMaxReps;
    @JsonProperty(value = "percent")
    public int percent;

    public ExerciseRequest() {}


    public static Exercise convertRequest(Exercise exercise, ExerciseRequest request) {
        if (exercise == null) {
            if(request.type == ExerciseType.EXERCISE) {
                exercise = new Exercise();
            } else if (request.type == ExerciseType.COMPLEX) {
                exercise = new ComplexExercise();
            } else if (request.type == ExerciseType.DURATION) {
                exercise = new DurationExercise();
            } else if (request.type == ExerciseType.ACCESSORY) {
                exercise = new AccessoryExercise();
            }

        }

        for (AthleteExerciseCommentRequest commentRequest : request.comments) {
            AthleteExerciseComment comment = AthleteExerciseCommentRequest.convertRequest(commentRequest);
            comment.setExercise(exercise);
            exercise.getComments().add(comment);
        }

        exercise.setId(request.id);
        exercise.setWeight(request.weight);
        exercise.setSets(request.sets);
        exercise.setWeightCompleted(request.weightCompleted);
        exercise.setIsMax(request.isMax);
        exercise.setNotes(request.notes);
        exercise.setIsComplete(request.isComplete);
        exercise.setOrder(request.order);
        exercise.setSetsCompleted(request.setsCompleted);
        exercise.setPercent(request.percent);

        if (request.type == ExerciseType.EXERCISE) {
            exercise.setName(request.name);
            exercise.setRepsPerSet(request.repsPerSet);
            exercise.setTotalRepsCompleted(request.totalRepsCompleted);
            exercise.setIsMaxReps(request.isMaxReps);
        } else if (request.type == ExerciseType.COMPLEX) {
            ((ComplexExercise) exercise).setRepArr(
                    ComplexExercise.convertRepArrListToString(request.repArr)
            );
            ((ComplexExercise) exercise).setNameArr(
                    ComplexExercise.convertNameArrListToString(request.nameArr)
            );
            ((ComplexExercise) exercise).setRepCompletedArr(
                    ComplexExercise.convertRepCompletedArrListToString(request.repCompletedArr)
            );
        } else if (request.type == ExerciseType.DURATION) {
            ((DurationExercise) exercise).setName(request.name);
            ((DurationExercise) exercise).setEffortIntensity(request.effortIntensity);
            ((DurationExercise) exercise).setActualIntensity(request.actualIntensity);
            ((DurationExercise) exercise).setEquipment(request.equipment);
            ((DurationExercise) exercise).setDistanceMeters(request.distanceMeters);
            ((DurationExercise) exercise).setDistanceCompletedMeters(request.distanceCompletedMeters);
            ((DurationExercise) exercise).setSecondsPerSet(request.secondsPerSet);
            ((DurationExercise) exercise).setSecondsPerSetCompleted(request.secondsPerSetCompleted);
        } else if (request.type == ExerciseType.ACCESSORY) {
            ((AccessoryExercise) exercise).setName(request.name);
            ((AccessoryExercise) exercise).setEffortIntensity(request.effortIntensity);
            ((AccessoryExercise) exercise).setActualIntensity(request.actualIntensity);
            ((AccessoryExercise) exercise).setRepsPerSet(request.repsPerSet);
            ((AccessoryExercise) exercise).setTotalRepsCompleted(request.totalRepsCompleted);
        }


        if (request.dropSets != null && request.dropSets.size() > 0) {
            for (ExerciseRequest r : request.dropSets) {
                Exercise dropSet = ExerciseRequest.convertRequest(null, r);
                dropSet.setDropSetPercent(r.dropSetPercent);
                if (exercise.getWeightCompleted() > 0 && dropSet.getWeightCompleted() < 1) {
                    int percentOfTopSetWeight =
                            (int)Math.round((dropSet.getDropSetPercent() / 100.0) * exercise.getWeightCompleted());
                    dropSet.setWeightCompleted(percentOfTopSetWeight);
                    dropSet.setIsComplete(true);
                }

                exercise.addDropSet(dropSet);
                dropSet.setTopSet(exercise);
            }
        }

        return exercise;
    }
}
