package com.coachapp.coach_pc.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public class UpdateUserRequest {


    private UUID id;
    private String username;
    private String athleteName;

    public UpdateUserRequest() {}

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }
}
