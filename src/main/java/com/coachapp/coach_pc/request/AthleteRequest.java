package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Team;

import java.util.List;
import java.util.UUID;

public class AthleteRequest {

    private UUID id;
    private UUID teamId;
    private UUID coachId;
    private String name;
    private List<AthleteRecord> records;

    public AthleteRequest() {}

    public AthleteRequest(UUID id, UUID teamId, UUID coachId, String name, List<AthleteRecord> records) {
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

    public List<AthleteRecord> getRecords() {
        return records;
    }

    public static AthleteData convertRequest(AthleteRequest request, AthleteData athlete) {
        if (request.getCoachId() != null) {
            CoachData coach = new CoachData();
            coach.setId(request.getCoachId());
            athlete.setCoach(coach);
        } else {
            athlete.setCoach(null);
        }
        if (request.getTeamId() != null) {
            Team team = new Team();
            team.setId(request.getTeamId());
            athlete.setTeam(team);
        } else {
            athlete.setTeam(null);
        }
        athlete.setId(request.getId());
        if (request.getName() != null) {
            athlete.setName(request.getName());
        }

        // TODO: this needs attention
        if (request.getRecords().size() > 0 && request.getRecords().get(0).getId() != null) {
            request.getRecords().forEach(r -> {
                r.setAthlete(athlete);
            });
            athlete.setRecords(request.getRecords());
        }


        return athlete;
    }
}
