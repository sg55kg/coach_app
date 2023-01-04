package com.coachapp.coach_pc.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class AthleteProgramStats {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    private AthleteData athlete;
    private int totalWeight;
    private int totalReps;
    private int avgWeightPerWeek;
    private int avgRepsPerWeek;
    private int percentSnatch;
    private int percentClean;
    private int percentJerk;
    private int percentOverhead;
    private int percentSquats;
    private int percentPulls;
    private int percentLowerBody;
    private int percentUpperBody;
    private int percentAccessories;

    public AthleteProgramStats() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AthleteData getAthlete() {
        return athlete;
    }

    public void setAthlete(AthleteData athlete) {
        this.athlete = athlete;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getTotalReps() {
        return totalReps;
    }

    public void setTotalReps(int totalReps) {
        this.totalReps = totalReps;
    }

    public int getAvgWeightPerWeek() {
        return avgWeightPerWeek;
    }

    public void setAvgWeightPerWeek(int avgWeightPerWeek) {
        this.avgWeightPerWeek = avgWeightPerWeek;
    }

    public int getAvgRepsPerWeek() {
        return avgRepsPerWeek;
    }

    public void setAvgRepsPerWeek(int avgRepsPerWeek) {
        this.avgRepsPerWeek = avgRepsPerWeek;
    }

    public int getPercentSnatch() {
        return percentSnatch;
    }

    public void setPercentSnatch(int percentSnatch) {
        this.percentSnatch = percentSnatch;
    }

    public int getPercentClean() {
        return percentClean;
    }

    public void setPercentClean(int percentClean) {
        this.percentClean = percentClean;
    }

    public int getPercentJerk() {
        return percentJerk;
    }

    public void setPercentJerk(int percentJerk) {
        this.percentJerk = percentJerk;
    }

    public int getPercentOverhead() {
        return percentOverhead;
    }

    public void setPercentOverhead(int percentOverhead) {
        this.percentOverhead = percentOverhead;
    }

    public int getPercentSquats() {
        return percentSquats;
    }

    public void setPercentSquats(int percentSquats) {
        this.percentSquats = percentSquats;
    }

    public int getPercentPulls() {
        return percentPulls;
    }

    public void setPercentPulls(int percentPulls) {
        this.percentPulls = percentPulls;
    }

    public int getPercentLowerBody() {
        return percentLowerBody;
    }

    public void setPercentLowerBody(int percentLowerBody) {
        this.percentLowerBody = percentLowerBody;
    }

    public int getPercentUpperBody() {
        return percentUpperBody;
    }

    public void setPercentUpperBody(int percentUpperBody) {
        this.percentUpperBody = percentUpperBody;
    }

    public int getPercentAccessories() {
        return percentAccessories;
    }

    public void setPercentAccessories(int percentAccessories) {
        this.percentAccessories = percentAccessories;
    }
}
