package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.*;

import java.util.*;

public class ProgramRequest {

    private List<DayRequest> days;
    private String name;
    private Date startDate;
    private Date endDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
