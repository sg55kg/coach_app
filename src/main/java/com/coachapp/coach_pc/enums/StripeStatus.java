package com.coachapp.coach_pc.enums;

public enum StripeStatus {
    NEW("NEW"),
    ONBOARDING("ONBOARDING"),
    CREATED("CREATED");

    private final String value;

    private StripeStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
