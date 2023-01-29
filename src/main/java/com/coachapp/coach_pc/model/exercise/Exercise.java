package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.Day;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "exercise")
@Inheritance(strategy = InheritanceType.JOINED)
public class Exercise {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;
    private String name;
    private Integer sets;
    private Integer repsPerSet;
    private Integer weight;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="day_id", nullable = true)
    @JsonIgnore
    private Day day;
    private String notes;
    private boolean isMax;
    private Integer weightCompleted = 0;
    @Column(name = "reps_completed")
    private Integer totalRepsCompleted = 0;
    private Integer setsCompleted = 0;
    @ManyToOne
    @JoinColumn(name = "athlete_comment_id", referencedColumnName = "id")
    private AthleteData athleteCommentId;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "exercise", orphanRemoval = true)
    private List<AthleteExerciseComment> comments = new ArrayList<>();
    private boolean isComplete;
    @Column(name = "list_order")
    private int order;
    @Transient
    private final ExerciseType type = ExerciseType.EXERCISE;
    @OneToMany(mappedBy = "topSet", cascade = CascadeType.MERGE)
    private List<Exercise> dropSets = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "top_set")
    @JsonIgnore
    private Exercise topSet;
    private int dropSetPercent;


    public Exercise() {}

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
        return sets != null ? sets : 0;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getRepsPerSet() {
        return repsPerSet != null ? repsPerSet : 0;
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

    @JsonProperty(value="isMax")
    public boolean getIsMax() {
        return isMax;
    }

    @JsonProperty(value="isMax")
    public void setIsMax(boolean isMax) {
        this.isMax = isMax;
    }

    public Integer getWeight() {
        return weight != null ? weight : 0;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeightCompleted() {
        return weightCompleted != null ? weightCompleted : 0;
    }

    public void setWeightCompleted(Integer weightCompleted) {
        this.weightCompleted = weightCompleted;
    }

    public Integer getTotalRepsCompleted() {
        return totalRepsCompleted != null ? totalRepsCompleted : 0;
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

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    @JsonProperty(value="isComplete")
    public boolean getIsComplete() {
        return isComplete;
    }

    @JsonProperty(value="isComplete")
    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getSetsCompleted() {
        return setsCompleted != null ? setsCompleted : 0;
    }

    public void setSetsCompleted(Integer setsCompleted) {
        this.setsCompleted = setsCompleted;
    }

    public ExerciseType getType() {
        return type;
    }

    public void addComment(AthleteExerciseComment comment) {
        this.comments.add(comment);
    }

    public void removeComment(AthleteExerciseComment comment) {
        this.comments.remove(comment);
    }


    public List<Exercise> getDropSets() {
        return dropSets;
    }

    public void setDropSets(List<Exercise> dropSets) {
        this.dropSets = dropSets;
    }


    public Exercise getTopSet() {
        return topSet;
    }

    public void setTopSet(Exercise topSet) {
        this.topSet = topSet;
    }

    public void addDropSet(Exercise set) {
        this.dropSets.add(set);
    }

    public void removeDropSet(Exercise set) {
        this.dropSets.remove(set);
    }

    public int getDropSetPercent() {
        return dropSetPercent;
    }

    public void setDropSetPercent(int dropSetPercent) {
        this.dropSetPercent = dropSetPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id.equals(exercise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
