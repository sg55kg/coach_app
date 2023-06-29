package com.coachapp.coach_pc.model.program;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class WarmUp {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    @JoinColumn(name="day_id", nullable = false)
    @JsonIgnore
    private Day day;
    private String instructions;

    // In Java, if you don't explicitly define any constructor in a class,
    // the compiler automatically provides
    // a default constructor with no arguments (also known as a no-arg constructor).
    // so this is not needed
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

    // Recommend using @EqualsAndHashCode from lombok
    // https://projectlombok.org/features/EqualsAndHashCode
    // this annotation will generate these for you and make it more readable, maintainable and less prone to bugs
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
