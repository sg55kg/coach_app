package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Team;

import java.util.UUID;

public class NewTeamRequest {

    private String name;
    private String description;
    private UUID coachId;

    public NewTeamRequest() {}

    public NewTeamRequest(String name, String description, UUID coachId) {
        this.name = name;
        this.description = description;
        this.coachId = coachId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public Team convertRequest(NewTeamRequest request) {
        Team team = new Team();

        team.setDescription(request.getDescription());
        team.setName(request.getName());

        return team;
    }
}
