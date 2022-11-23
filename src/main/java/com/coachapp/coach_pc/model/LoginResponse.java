package com.coachapp.coach_pc.model;

import org.keycloak.representations.AccessTokenResponse;

public class LoginResponse {

    private AccessTokenResponse token;
    private UserData userData;

    public LoginResponse() {}

    public LoginResponse(AccessTokenResponse token, UserData userData) {
        this.token = token;
        this.userData = userData;
    }

    public AccessTokenResponse getToken() {
        return token;
    }

    public void setToken(AccessTokenResponse token) {
        this.token = token;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
