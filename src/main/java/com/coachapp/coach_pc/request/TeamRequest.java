package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Team;

import java.util.UUID;

public class TeamRequest {

    private UUID id;
    private String name;
    private String description;
    private String teamLogo;

    public TeamRequest() {}

    public TeamRequest(UUID id, String name, String description, String teamLogo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teamLogo = teamLogo;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getTeamLogo() {
        return teamLogo;
    }

    public static Team convertRequest(Team team, TeamRequest request) {

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setTeamLogo(request.getTeamLogo());

        return team;
    }
}
