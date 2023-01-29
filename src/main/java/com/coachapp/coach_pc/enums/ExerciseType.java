package com.coachapp.coach_pc.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExerciseType {
    EXERCISE,
    CARRY,
    DURATION,
    ACCESSORY,
    COMPLEX;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
