package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.request.program.ProgramRequest;

import java.util.List;
import java.util.UUID;

public class CoachRequest {

    private UUID id;

    private List<ProgramRequest> programs;


    private List<AthleteData> athletes;

    public CoachRequest() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<ProgramRequest> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramRequest> programs) {
        this.programs = programs;
    }


    public List<AthleteData> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<AthleteData> athletes) {
        this.athletes = athletes;
    }
}
