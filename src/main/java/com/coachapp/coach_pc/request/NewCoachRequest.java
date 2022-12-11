package com.coachapp.coach_pc.request;

import java.util.UUID;

public class NewCoachRequest {

    private UUID userId;
    private String email;
    private String username;


    public NewCoachRequest(UUID userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
