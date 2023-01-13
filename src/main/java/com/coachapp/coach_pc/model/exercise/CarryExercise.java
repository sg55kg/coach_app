package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.exercise.Exercise;

import javax.persistence.Entity;

@Entity
public class CarryExercise extends Exercise {

    private int distanceMeters;
    private boolean unilateral = false;
    private int distanceCompletedMeters = 0;
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private EffortIntensity effortIntensity = EffortIntensity.EASY;

    public CarryExercise() {}

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public boolean getUnilateral() {
        return unilateral;
    }

    public void setUnilateral(boolean unilateral) {
        this.unilateral = unilateral;
    }

    public int getDistanceCompletedMeters() {
        return distanceCompletedMeters;
    }

    public void setDistanceCompletedMeters(int distanceCompletedMeters) {
        this.distanceCompletedMeters = distanceCompletedMeters;
    }

    public WeightIntensity getWeightIntensity() {
        return weightIntensity;
    }

    public void setWeightIntensity(WeightIntensity weightIntensity) {
        this.weightIntensity = weightIntensity;
    }

    public EffortIntensity getEffortIntensity() {
        return effortIntensity;
    }

    public void setEffortIntensity(EffortIntensity effortIntensity) {
        this.effortIntensity = effortIntensity;
    }
}
