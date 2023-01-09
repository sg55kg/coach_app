package com.coachapp.coach_pc.model;

import com.coachapp.coach_pc.enums.WeightIntensity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer sets;
    private Integer repsPerSet;
    private Integer weight;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="day_id", nullable = false)
    @JsonIgnore
    private Day day;
    private String notes;
    @Nullable
    private WeightIntensity weightIntensity = WeightIntensity.NONE;
    private Boolean isMax = false;
    private Integer weightCompleted = 0;
    @Column(name = "reps_completed")
    private Integer totalRepsCompleted = 0;
    @ManyToOne
    @JoinColumn(name = "athlete_comment_id", referencedColumnName = "id")
    private AthleteData athleteCommentId;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "exercise", orphanRemoval = true)
    private List<AthleteExerciseComment> comments;
    private Boolean isComplete;
    @Column(name = "list_order")
    private int order;


    public Exercise() {}

    public Exercise(
            UUID id,
            String name,
            Integer sets,
            Integer repsPerSet,
            Day day,
            String notes,
            WeightIntensity weightIntensity,
            boolean isMax,
            Integer weight,
            Integer weightCompleted,
            Integer totalRepsCompleted,
            boolean isComplete,
            int order
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
        this.isComplete = isComplete;
        this.order = order;
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

    public Integer getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(Integer repsPerSet) {
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getMax() {
        return isMax;
    }

    public Integer getWeightCompleted() {
        return weightCompleted;
    }

    public void setWeightCompleted(Integer weightCompleted) {
        this.weightCompleted = weightCompleted;
    }

    public Integer getTotalRepsCompleted() {
        return totalRepsCompleted;
    }

    public void setTotalRepsCompleted(Integer totalRepsCompleted) {
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

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public boolean getIsComplete() {
        return isComplete != null ? isComplete : false;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
