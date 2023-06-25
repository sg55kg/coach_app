package com.coachapp.coach_pc.request.payment;

import java.util.UUID;

public class NewStripeAccountRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private UUID coachId;
    private UUID teamId;
    private String email;
    private String username;

    // recommend setting defaults here
    private String countryCode = "";
    private String currency = "";
    private int athleteCap = 0;

    public NewStripeAccountRequest() {}

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public int getAthleteCap() {
        return athleteCap;
    }

    public void setAthleteCap(int athleteCap) {
        this.athleteCap = athleteCap;
    }
}
