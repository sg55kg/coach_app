package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.user.UserPreference;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public class UpdateUserRequest {


    private UUID id;
    private String username;
    private String athleteName;
    private String photoUrl;
    private UserPreference preferences;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public UserPreference getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreference preferences) {
        this.preferences = preferences;
    }
}
