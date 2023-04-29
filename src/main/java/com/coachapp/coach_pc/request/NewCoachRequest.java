package com.coachapp.coach_pc.request;

import java.util.UUID;

public class NewCoachRequest {

    private NewTeamRequest team;
    private UUID userId;

    public NewCoachRequest() {}

    public void setTeam(NewTeamRequest team) {
        this.team = team;
    }

    public NewTeamRequest getTeam() {
        return team;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
