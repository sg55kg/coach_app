package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Team;

import java.util.UUID;

public class AthleteRequest {

    private UUID id;
    private UUID teamId;
    private UUID coachId;
    private String name;
    private AthleteRecord records;

    public AthleteRequest() {}

    public AthleteRequest(UUID id, UUID teamId, UUID coachId, String name, AthleteRecord records) {
        this.id = id;
        this.teamId = teamId;
        this.coachId = coachId;
        this.name = name;
        this.records = records;
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

    public AthleteRecord getRecords() {
        return records;
    }

    public static AthleteData convertRequest(AthleteRequest request, AthleteData athlete) {
        Team team = new Team();
        CoachData coach = new CoachData();

        team.setId(request.getTeamId());
        coach.setId(request.getCoachId());

        athlete.setId(request.getId());
        if (request.getName() != null) {
            athlete.setName(request.getName());
        }
        athlete.setTeam(team);
        athlete.setCoach(coach);
        if (request.getRecords() != null && request.getRecords().getId() != null) {
            athlete.setRecords(request.getRecords());
        }


        return athlete;
    }
}
