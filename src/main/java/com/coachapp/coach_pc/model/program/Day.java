package com.coachapp.coach_pc.model.program;

import com.coachapp.coach_pc.model.exercise.Exercise;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "days")
public class Day {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    private OffsetDateTime date;
    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    @JsonIgnore
    private Program program;
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exercise> exercises;
    private boolean isRestDay;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "warm_up_id")
    private WarmUp warmUp;

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

    public WarmUp getWarmUp() {
        return warmUp;
    }

    public void setWarmUp(WarmUp warmUp) {
        this.warmUp = warmUp;
    }

    // Recommend using @EqualsAndHashCode from lombok
    // https://projectlombok.org/features/EqualsAndHashCode
    // this annotation will generate these for you and make it more readable, maintainable and less prone to bugs
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
