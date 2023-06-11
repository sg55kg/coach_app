package com.coachapp.coach_pc.enums;

public enum AthleteTeamStatus {
    INVITED("INVITED"),
    CURRENT("CURRENT"),
    UNPAID("UNPAID"),
    BLOCKED("BLOCKED"),
    INACTIVE("INACTIVE");

    private final String value;

    private AthleteTeamStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
