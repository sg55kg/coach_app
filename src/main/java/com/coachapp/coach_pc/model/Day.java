package com.coachapp.coach_pc.model;

import com.coachapp.coach_pc.request.DayRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    private OffsetDateTime date;
    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    @JsonIgnore
    private Program program;
    @OneToMany(mappedBy = "day", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exercise> exercises;
    private boolean isRestDay;

    public Day() {}

    public Day(OffsetDateTime date, List<Exercise> exercises, UUID id, Program program, boolean isRestDay) {
        this.date = date;
        this.exercises = exercises;
        this.id = id;
        this.program = program;
        this.isRestDay = isRestDay;
    }

    public Day(OffsetDateTime date, List<Exercise> exercises) {
        this.date = date;
        this.exercises = exercises;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        exercises.forEach(e -> e.setDay(this));
    }

    public boolean getIsRestDay() {
        return isRestDay;
    }

    public void setIsRestDay(boolean isRestDay) {
        this.isRestDay = isRestDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return id.equals(day.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
