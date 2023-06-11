package com.coachapp.coach_pc.request.payment;

import java.util.UUID;

public class AthletePaymentRequest {

    private UUID teamId;
    private UUID coachId;
    private UUID athleteId;
    private UUID teamFinanceId;
    private long price;
    private String athleteEmail;
    private String stripeConnectId;

    public AthletePaymentRequest() {}

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAthleteEmail() {
        return athleteEmail;
    }

    public void setAthleteEmail(String athleteEmail) {
        this.athleteEmail = athleteEmail;
    }

    public UUID getTeamFinanceId() {
        return teamFinanceId;
    }

    public void setTeamFinanceId(UUID teamFinanceId) {
        this.teamFinanceId = teamFinanceId;
    }

    public String getStripeConnectId() {
        return stripeConnectId;
    }

    public void setStripeConnectId(String stripeConnectId) {
        this.stripeConnectId = stripeConnectId;
    }
}
