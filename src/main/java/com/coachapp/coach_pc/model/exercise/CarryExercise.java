package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

// There is no universally accepted convention for naming SQL tables, but many developers and database administrators prefer to use plural table names.
// The reasoning behind this is that a table typically represents a collection of similar entities, and the plural form better reflects this concept.
// e.g carry_exercises (each record being a carry_exercise)
@Entity
@Table(name = "carry_exercise")
public class CarryExercise extends Exercise {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors
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

    @Override
    public ExerciseType getType() {
        return type;
    }
}
