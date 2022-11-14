package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.Day;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ProgramRequest {

    private UUID id;
    private Set<DayRequest> days;
    private String name;
    private Date startDate;
    private Date endDate;

    public ProgramRequest() {}

    public ProgramRequest(UUID id, Set<DayRequest> days, String name) {
        this.id = id;
        this.days = days;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Set<DayRequest> getDays() {
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
