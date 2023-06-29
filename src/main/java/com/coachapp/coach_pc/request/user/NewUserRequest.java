package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.model.user.UserPreference;

public class NewUserRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
