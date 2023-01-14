package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "carry_exercise")
public class CarryExercise extends Exercise {

    private int distanceMeters;
    private boolean unilateral = false;
    private int distanceCompletedMeters = 0;
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private EffortIntensity effortIntensity = EffortIntensity.EASY;
    @Transient
    private final ExerciseType type = ExerciseType.CARRY;

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
