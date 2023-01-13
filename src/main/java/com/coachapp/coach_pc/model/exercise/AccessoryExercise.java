package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.exercise.Exercise;

import javax.persistence.*;

@Entity
public class AccessoryExercise extends Exercise {

    private String equipment;
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private EffortIntensity effortIntensity = EffortIntensity.EASY;
    private boolean unilateral = false;

    public AccessoryExercise() {}

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
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

    public boolean getUnilateral() {
        return unilateral;
    }

    public void setUnilateral(boolean unilateral) {
        this.unilateral = unilateral;
    }
}
