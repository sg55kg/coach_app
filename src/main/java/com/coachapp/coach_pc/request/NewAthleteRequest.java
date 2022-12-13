package com.coachapp.coach_pc.request;

import java.util.UUID;

public class NewAthleteRequest {

    private UUID userId;
    private String name;

    public NewAthleteRequest() {

    }

    public NewAthleteRequest(UUID userId, String name) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
