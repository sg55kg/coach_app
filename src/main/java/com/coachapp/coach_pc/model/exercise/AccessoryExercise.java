package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;

import javax.persistence.*;

// There is no universally accepted convention for naming SQL tables, but many developers and database administrators prefer to use plural table names.
// The reasoning behind this is that a table typically represents a collection of similar entities, and the plural form better reflects this concept.
// e.g accessory_exercises (each record being a accessory_exercise)
@Entity
@Table(name = "accessory_exercise")
public class AccessoryExercise extends Exercise {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
