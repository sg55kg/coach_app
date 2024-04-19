package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.request.team.NewTeamRequest;

import java.util.UUID;

public class NewCoachRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
