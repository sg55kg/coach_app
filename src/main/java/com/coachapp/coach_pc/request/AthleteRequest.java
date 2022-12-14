package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Team;

import java.util.UUID;

public class AthleteRequest {

    private UUID id;
    private UUID teamId;
    private UUID coachId;
    private String name;

    public AthleteRequest() {}

    public AthleteRequest(UUID id, UUID teamId, UUID coachId, String name) {
        this.id = id;
        this.teamId = teamId;
        this.coachId = coachId;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public String getName() {
        return name;
    }

    public static AthleteData convertRequest(AthleteRequest request) {
        AthleteData athlete = new AthleteData();
        Team team = new Team();
        CoachData coach = new CoachData();

        team.setId(request.getTeamId());
        coach.setId(request.getCoachId());

        athlete.setId(request.getId());
        athlete.setName(request.getName());
        athlete.setTeam(team);
        athlete.setCoach(coach);

        return athlete;
    }
}
