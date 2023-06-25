package com.coachapp.coach_pc.request.program;

import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.Program;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateProgramRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private UUID id;
    private String name;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    private List<DayRequest> days;

    public UpdateProgramRequest(UUID id, String name, OffsetDateTime startDate, OffsetDateTime endDate) {
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

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public OffsetDateTime getEndDate() {
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
