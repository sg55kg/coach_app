package com.coachapp.coach_pc.request.user;

import java.util.UUID;

public class InviteUserRequest {

    private String coachEmail;
    private String athleteEmail;
    private String athleteName;
    private String coachName;
    private UUID teamId;
    private UUID coachId;

    public InviteUserRequest() {}

    public String getCoachEmail() {
        return coachEmail;
    }

    public void setCoachEmail(String coachEmail) {
        this.coachEmail = coachEmail;
    }

    public String getAthleteEmail() {
        return athleteEmail;
    }

    public void setAthleteEmail(String athleteEmail) {
        this.athleteEmail = athleteEmail;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

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

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
}
