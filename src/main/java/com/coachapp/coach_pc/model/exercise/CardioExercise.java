package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cardio_exercise")
public class CardioExercise extends Exercise {

    private String equipment;
    private EffortIntensity effortIntensity = EffortIntensity.EASY;
    private EffortIntensity actualIntensity = EffortIntensity.EASY;
    private int distanceMeters;
    private int distanceCompletedMeters;
    private int secondsPerSet;
    private int secondsPerSetCompleted;
    @Transient
    private final ExerciseType type = ExerciseType.CARDIO;

    public CardioExercise() {}

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
}
