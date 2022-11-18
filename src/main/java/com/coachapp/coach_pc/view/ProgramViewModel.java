package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProgramViewModel {

    private UUID id;
    private String name;
    private Date startDate;
    private Date endDate;
    private List<DayViewModel> days;

    public ProgramViewModel() {

    }

    public ProgramViewModel(UUID id, String name, Date startDate, Date endDate, List<DayViewModel> days) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<DayViewModel> getDays() {
        return days;
    }

    public void setDays(List<DayViewModel> days) {
        this.days = days;
    }
}
