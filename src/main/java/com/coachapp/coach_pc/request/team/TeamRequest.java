package com.coachapp.coach_pc.request.team;

import com.coachapp.coach_pc.model.team.Team;

import java.util.UUID;

public class TeamRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
