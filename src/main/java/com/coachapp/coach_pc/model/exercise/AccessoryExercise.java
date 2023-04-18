package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;

import javax.persistence.*;

@Entity
@Table(name = "accessory_exercise")
public class AccessoryExercise extends Exercise {

    private String equipment;
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private EffortIntensity effortIntensity = EffortIntensity.EASY;
    private EffortIntensity actualIntensity;
    private boolean unilateral = false;
    @Transient
    private final ExerciseType type = ExerciseType.ACCESSORY;

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

    public void setUnilateral(boolean Unilateral) {
        this.unilateral = unilateral;
    }

    public EffortIntensity getActualIntensity() {
        return actualIntensity;
    }

    public void setActualIntensity(EffortIntensity actualIntensity) {
        this.actualIntensity = actualIntensity;
    }

    @Override
    public ExerciseType getType() {
        return type;
    }
}
