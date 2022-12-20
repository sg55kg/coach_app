package com.coachapp.coach_pc.model;

import com.coachapp.coach_pc.enums.WeightIntensity;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    private String name;
    private int sets;
    private int repsPerSet;
    private int weight;
    @ManyToOne
    @JoinColumn(name="day_id", nullable = false)
    private Day day;
    private String notes;
    @Nullable
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private Boolean isMax = false;
    private int weightCompleted = 0;
    private int totalRepsCompleted = 0;
    @ManyToOne
    @JoinColumn(name = "athlete_comment_id", referencedColumnName = "id")
    private AthleteData athleteCommentId;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<AthleteExerciseComment> comments;


    public Exercise() {}

    public Exercise(
            UUID id,
            String name,
            int sets,
            int repsPerSet,
            Day day,
            String notes,
            WeightIntensity weightIntensity,
            boolean isMax,
            int weight,
            int weightCompleted,
            int totalRepsCompleted
    ) {
        this.id = id;
        this.name = name;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.day = day;
        this.notes = notes;
        this.weightIntensity = weightIntensity;
        this.isMax = isMax;
        this.weight = weight;
        this.weightCompleted = weightCompleted;
        this.totalRepsCompleted = totalRepsCompleted;
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

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public WeightIntensity getWeightIntensity() {
        return weightIntensity;
    }

    public void setWeightIntensity(WeightIntensity weightIntensity) {
        this.weightIntensity = weightIntensity;
    }

    public Boolean isMax() {
        return isMax != null ? isMax : false;
    }

    public void setIsMax(boolean isMax) {
        this.isMax = isMax;
    }

    public void setMax(Boolean max) {
        isMax = max;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Boolean getMax() {
        return isMax;
    }

    public int getWeightCompleted() {
        return weightCompleted;
    }

    public void setWeightCompleted(int weightCompleted) {
        this.weightCompleted = weightCompleted;
    }

    public int getTotalRepsCompleted() {
        return totalRepsCompleted;
    }

    public void setTotalRepsCompleted(int totalRepsCompleted) {
        this.totalRepsCompleted = totalRepsCompleted;
    }

    public AthleteData getAthleteCommentId() {
        return athleteCommentId;
    }

    public void setAthleteCommentId(AthleteData athleteCommentId) {
        this.athleteCommentId = athleteCommentId;
    }

    public List<AthleteExerciseComment> getComments() {
        return comments;
    }

    public void setComments(List<AthleteExerciseComment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id.equals(exercise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
