package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.model.exercise.Exercise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

// There is no universally accepted convention for naming SQL tables, but many developers and database administrators prefer to use plural table names.
// The reasoning behind this is that a table typically represents a collection of similar entities, and the plural form better reflects this concept.
// e.g athlete_exercise_comment (each record being a athlete_exercise_comment)
@Entity
@Table(name = "athlete_exercise_comment")
public class AthleteExerciseComment {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @Column(name = "athlete_id")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID athleteId;
    private String content;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    @JsonIgnore
    private Exercise exercise;
    private String commenterName;

    public AthleteExerciseComment() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }
}
