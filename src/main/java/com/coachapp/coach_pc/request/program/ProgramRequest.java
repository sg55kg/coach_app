package com.coachapp.coach_pc.request.program;

import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.Program;

import java.time.OffsetDateTime;
import java.util.*;

public class ProgramRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private List<DayRequest> days;
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private UUID coachId;
    private UUID athleteId;
    private UUID teamId;
    private boolean isCurrent;

    public ProgramRequest() {}

    public ProgramRequest(List<DayRequest> days, String name) {
        this.days = days;
        this.name = name;
    }


    public List<DayRequest> getDays() {
        return days;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public static Program convertRequest(ProgramRequest request) {
        Program program = new Program();

        program.setName(request.getName());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());

        List<Day> days = new ArrayList<>();

        for (DayRequest dayRequest : request.getDays()) {
            Day day = DayRequest.convertRequest(dayRequest, program);
            days.add(day);
        }
        program.setDays(days);

        return program;
    }

    // use lombok for equals and hash code

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ProgramRequest that = (ProgramRequest) o;
//        return Objects.equals(id, that.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
