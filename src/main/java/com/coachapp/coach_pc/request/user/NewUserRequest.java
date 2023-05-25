package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.model.user.UserPreference;

public class NewUserRequest {

    private String email;
    private String name;
    private String photoUrl;
    private UserPreference preferences;

    public NewUserRequest(String email, String name, String photoUrl, UserPreference preferences) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.preferences = preferences;
    }

    public NewUserRequest() {}

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
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
