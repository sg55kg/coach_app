package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.UserData;

import java.util.*;

public class ProgramRequest {

    private UUID id;
    private List<DayRequest> days;
    private String name;
    private Date startDate;
    private Date endDate;
    private UserData coach;

    public ProgramRequest() {}

    public ProgramRequest(UUID id, List<DayRequest> days, String name, UserData coach) {
        this.id = id;
        this.days = days;
        this.name = name;
        this.coach = coach;
    }

    public UUID getId() {
        return id;
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

    public UserData getCoach() {
        return coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramRequest that = (ProgramRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
