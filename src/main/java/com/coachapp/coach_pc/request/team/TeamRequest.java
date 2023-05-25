package com.coachapp.coach_pc.request.team;

import com.coachapp.coach_pc.model.team.Team;

import java.util.UUID;

public class TeamRequest {

    private UUID id;
    private String name;
    private String description;
    private String teamLogo;

    public TeamRequest() {}

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeamLogo(String teamLogo) {
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
