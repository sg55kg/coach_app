package com.coachapp.coach_pc.model;

import com.coachapp.coach_pc.request.DayRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exercise> exercises;

    public Day() {}

    public Day(Date date, List<Exercise> exercises, UUID id, Program program) {
        this.date = date;
        this.exercises = exercises;
        this.id = id;
        this.program = program;
    }

    public Day(Date date, List<Exercise> exercises) {
        this.date = date;
        this.exercises = exercises;
    }

    public static Day convertRequest(DayRequest dayRequest, Program program) {
        Day day = new Day();
        List<Exercise> exercises = new ArrayList<>();

        day.setProgram(program);
        day.setDate(dayRequest.getDate());
        day.setId(dayRequest.getId());

        dayRequest.getExercises().forEach(e ->
                exercises.add(new Exercise(
                        e.getId(),
                        e.getName(),
                        e.getSets(),
                        e.getRepsPerSet(),
                        e.getDay(),
                        e.getNotes(),
                        e.getWeightIntensity(),
                        e.isMax()))
                );
        day.setExercises(exercises);
        return day;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
