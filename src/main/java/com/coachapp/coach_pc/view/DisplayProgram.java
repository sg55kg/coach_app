package com.coachapp.coach_pc.view;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

public class DisplayProgram {

    private UUID id;
    private Date createdAt;
    private Date updatedAt;
    private Date startDate;
    private Date endDate;
    private OffsetDateTime lastEnteredDay;
    private String name;

    public DisplayProgram(UUID id, Date createdAt, Date updatedAt, Date startDate, Date endDate, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getLastEnteredDay() {
        return lastEnteredDay;
    }

    public void setLastEnteredDay(OffsetDateTime lastEnteredDay) {
        this.lastEnteredDay = lastEnteredDay;
    }
}
