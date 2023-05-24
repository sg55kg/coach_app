package com.coachapp.coach_pc.enums;

public enum PaymentStatus {
    NEW("NEW"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private final String value;

    private PaymentStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
