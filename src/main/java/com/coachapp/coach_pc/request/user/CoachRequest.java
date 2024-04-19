package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.request.program.ProgramRequest;

import java.util.List;
import java.util.UUID;

public class CoachRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
