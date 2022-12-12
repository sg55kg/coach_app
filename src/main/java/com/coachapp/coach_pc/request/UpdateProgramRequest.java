package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UpdateProgramRequest {

    private UUID id;
    private String name;
    private Date startDate;
    private Date endDate;

    private List<DayRequest> days;

    public UpdateProgramRequest(UUID id, String name, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<DayRequest> getDays() {
        return days;
    }

    public static Program convertRequest(UpdateProgramRequest request, Program dbProgram) {
        Program program = new Program();

        program.setId(request.getId());
        program.setName(request.getName());
        program.setCoach(dbProgram.getCoach());
        program.setAthlete(dbProgram.getAthlete());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());
        program.setCreatedAt(dbProgram.getCreatedAt());

        List<Day> days = new ArrayList<>();

        for (DayRequest dayRequest : request.getDays()) {
            Day day = DayRequest.convertRequest(dayRequest, program);
            days.add(day);
        }
        program.setDays(days);

        return program;
    }
}
