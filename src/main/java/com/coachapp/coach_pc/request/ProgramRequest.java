package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Day;

import java.util.Set;
import java.util.UUID;

public class ProgramRequest {

    private UUID id;
    private Set<Day> days;
    private String name;

    public ProgramRequest() {}

    public ProgramRequest(UUID id, Set<Day> days, String name) {
        this.id = id;
        this.days = days;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Set<Day> getDays() {
        return days;
    }

    public String getName() {
        return name;
    }
}
