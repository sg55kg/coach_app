package com.coachapp.coach_pc.request;

public class NewUserRequest {

    private String email;
    private String name;
    private String photoUrl;

    public NewUserRequest(String email, String name, String photoUrl) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
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
}
