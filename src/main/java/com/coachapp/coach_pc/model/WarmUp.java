package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class WarmUp {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    @JoinColumn(name="day_id", nullable = false)
    @JsonIgnore
    private Day day;
    private String instructions;

    public WarmUp() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarmUp warmUp = (WarmUp) o;
        return Objects.equals(id, warmUp.id) && Objects.equals(day, warmUp.day) && Objects.equals(instructions, warmUp.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, instructions);
    }
}
