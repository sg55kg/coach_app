package com.coachapp.coach_pc.request.team;

import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.model.team.Team;

import java.util.List;
import java.util.UUID;

public class NewTeamRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private String name;
    private String description;
    private UUID coachId;
    private String teamLogo;

    public NewTeamRequest() {}

    public NewTeamRequest(String name, String description, UUID coachId, String teamLogo) {
        this.name = name;
        this.description = description;
        this.coachId = coachId;
        this.teamLogo = teamLogo;
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
    public String getTeamLogo() {
        return teamLogo;
    }

    public static Team convertRequest(NewTeamRequest request, CoachData coach) {
        Team team = new Team();

        team.setDescription(request.getDescription());
        team.setName(request.getName());
        team.setCoach(coach);
        team.setTeamLogo(request.getTeamLogo());
        coach.setTeams(List.of(team));

        return team;
    }
}
