package com.coachapp.coach_pc.request;

public class NewUserRequest {

    private String email;
    private String name;

    public NewUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
