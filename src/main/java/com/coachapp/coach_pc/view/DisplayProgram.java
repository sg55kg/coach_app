package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.Program;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

public class DisplayProgram {

    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private OffsetDateTime lastEnteredDay;
    private String name;
    private UUID athleteId;

    public DisplayProgram() {}

    public DisplayProgram(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime startDate, OffsetDateTime endDate, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.athleteId = athleteId;
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getLastEnteredDay() {
        return lastEnteredDay;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setLastEnteredDay(OffsetDateTime lastEnteredDay) {
        this.lastEnteredDay = lastEnteredDay;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public static DisplayProgram convertProgramForDisplay(Program program) {
        DisplayProgram displayProgram = new DisplayProgram();

        OffsetDateTime lastEntered = null;
        int numProgramDays = program.getDays().size();

        if (numProgramDays > 0) {
            lastEntered = program.getDays().get(numProgramDays-1).getDate();
        }

        displayProgram.setId(program.getId());
        displayProgram.setCreatedAt(program.getCreatedAt());
        displayProgram.setName(program.getName());
        displayProgram.setUpdatedAt(program.getUpdatedAt());
        displayProgram.setStartDate(program.getStartDate());
        displayProgram.setEndDate(program.getEndDate());
        displayProgram.setLastEnteredDay(lastEntered);

        if (program.getAthlete() != null) {
            displayProgram.setAthleteId(program.getAthlete().getId());
        }

        return displayProgram;
    }
}
