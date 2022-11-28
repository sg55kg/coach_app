package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.UserData;

public class LoginResponse {

    private TokenResponse token;
    private UserData userData;

    public LoginResponse() {}

    public LoginResponse(TokenResponse token, UserData userData) {
        this.token = token;
        this.userData = userData;
    }

    public TokenResponse getToken() {
        return token;
    }

    public void setToken(TokenResponse token) {
        this.token = token;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
