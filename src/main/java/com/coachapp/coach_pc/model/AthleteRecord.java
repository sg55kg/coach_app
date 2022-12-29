package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class AthleteRecord {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @OneToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "id")
    @JsonIgnore
    private AthleteData athlete;
    private int snatch;
    private int cleanAndJerk;
    private int jerk;
    private int clean;
    private int backSquat;
    private int frontSquat;
    private int deadlift;
    private int snatchDeadlift;
    private int pushPress;
    private int strictPress;
    private int bentOverRow;
    private int powerSnatch;
    private int powerClean;
    private int powerJerk;
    private int hangSnatch;
    private int hangPowerSnatch;
    private int hangPowerClean;
    private int blockSnatch;
    private int blockClean;
    private int blockPowerSnatch;
    private int blockPowerClean;
    private int cleanPull;
    private int snatchPull;
    private int snatchHighPull;
    private int benchPress;
    private int pendlayRow;
    private int snatchPushPress;
    private int overheadSquat;
    private int squatJerk;
    private int maxPullUps;
    private int weightedPullUp;
    private int maxChinUps;
    private int weightedChinUp;
    private int pauseSnatch;
    private int pauseClean;
    private int sandbagCarryMeters;
    private int sandbagCarryWeight;
    private int farmerCarryMeters;
    private int farmerCarryWeight;

    public AthleteRecord() {}

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

    public int getSnatch() {
        return snatch;
    }

    public void setSnatch(int snatch) {
        this.snatch = snatch;
    }

    public int getCleanAndJerk() {
        return cleanAndJerk;
    }

    public void setCleanAndJerk(int cleanAndJerk) {
        this.cleanAndJerk = cleanAndJerk;
    }

    public int getJerk() {
        return jerk;
    }

    public void setJerk(int jerk) {
        this.jerk = jerk;
    }

    public int getClean() {
        return clean;
    }

    public void setClean(int clean) {
        this.clean = clean;
    }

    public int getBackSquat() {
        return backSquat;
    }

    public void setBackSquat(int backSquat) {
        this.backSquat = backSquat;
    }

    public int getFrontSquat() {
        return frontSquat;
    }

    public void setFrontSquat(int frontSquat) {
        this.frontSquat = frontSquat;
    }

    public int getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(int deadlift) {
        this.deadlift = deadlift;
    }

    public int getSnatchDeadlift() {
        return snatchDeadlift;
    }

    public void setSnatchDeadlift(int snatchDeadlift) {
        this.snatchDeadlift = snatchDeadlift;
    }

    public int getPushPress() {
        return pushPress;
    }

    public void setPushPress(int pushPress) {
        this.pushPress = pushPress;
    }

    public int getStrictPress() {
        return strictPress;
    }

    public void setStrictPress(int strictPress) {
        this.strictPress = strictPress;
    }

    public int getBentOverRow() {
        return bentOverRow;
    }

    public void setBentOverRow(int bentOverRow) {
        this.bentOverRow = bentOverRow;
    }

    public int getPowerSnatch() {
        return powerSnatch;
    }

    public void setPowerSnatch(int powerSnatch) {
        this.powerSnatch = powerSnatch;
    }

    public int getPowerClean() {
        return powerClean;
    }

    public void setPowerClean(int powerClean) {
        this.powerClean = powerClean;
    }

    public int getPowerJerk() {
        return powerJerk;
    }

    public void setPowerJerk(int powerJerk) {
        this.powerJerk = powerJerk;
    }

    public int getHangSnatch() {
        return hangSnatch;
    }

    public void setHangSnatch(int hangSnatch) {
        this.hangSnatch = hangSnatch;
    }

    public int getHangPowerSnatch() {
        return hangPowerSnatch;
    }

    public void setHangPowerSnatch(int hangPowerSnatch) {
        this.hangPowerSnatch = hangPowerSnatch;
    }

    public int getHangPowerClean() {
        return hangPowerClean;
    }

    public void setHangPowerClean(int hangPowerClean) {
        this.hangPowerClean = hangPowerClean;
    }

    public int getBlockSnatch() {
        return blockSnatch;
    }

    public void setBlockSnatch(int blockSnatch) {
        this.blockSnatch = blockSnatch;
    }

    public int getBlockClean() {
        return blockClean;
    }

    public void setBlockClean(int blockClean) {
        this.blockClean = blockClean;
    }

    public int getBlockPowerSnatch() {
        return blockPowerSnatch;
    }

    public void setBlockPowerSnatch(int blockPowerSnatch) {
        this.blockPowerSnatch = blockPowerSnatch;
    }

    public int getBlockPowerClean() {
        return blockPowerClean;
    }

    public void setBlockPowerClean(int blockPowerClean) {
        this.blockPowerClean = blockPowerClean;
    }

    public int getCleanPull() {
        return cleanPull;
    }

    public void setCleanPull(int cleanPull) {
        this.cleanPull = cleanPull;
    }

    public int getSnatchPull() {
        return snatchPull;
    }

    public void setSnatchPull(int snatchPull) {
        this.snatchPull = snatchPull;
    }

    public int getSnatchHighPull() {
        return snatchHighPull;
    }

    public void setSnatchHighPull(int snatchHighPull) {
        this.snatchHighPull = snatchHighPull;
    }

    public int getBenchPress() {
        return benchPress;
    }

    public void setBenchPress(int benchPress) {
        this.benchPress = benchPress;
    }

    public int getPendlayRow() {
        return pendlayRow;
    }

    public void setPendlayRow(int pendlayRow) {
        this.pendlayRow = pendlayRow;
    }

    public int getSnatchPushPress() {
        return snatchPushPress;
    }

    public void setSnatchPushPress(int snatchPushPress) {
        this.snatchPushPress = snatchPushPress;
    }

    public int getOverheadSquat() {
        return overheadSquat;
    }

    public void setOverheadSquat(int overheadSquat) {
        this.overheadSquat = overheadSquat;
    }

    public int getSquatJerk() {
        return squatJerk;
    }

    public void setSquatJerk(int squatJerk) {
        this.squatJerk = squatJerk;
    }

    public int getMaxPullUps() {
        return maxPullUps;
    }

    public void setMaxPullUps(int maxPullUps) {
        this.maxPullUps = maxPullUps;
    }

    public int getWeightedPullUp() {
        return weightedPullUp;
    }

    public void setWeightedPullUp(int weightedPullUp) {
        this.weightedPullUp = weightedPullUp;
    }

    public int getMaxChinUps() {
        return maxChinUps;
    }

    public void setMaxChinUps(int maxChinUps) {
        this.maxChinUps = maxChinUps;
    }

    public int getWeightedChinUp() {
        return weightedChinUp;
    }

    public void setWeightedChinUp(int weightedChinUp) {
        this.weightedChinUp = weightedChinUp;
    }

    public int getPauseSnatch() {
        return pauseSnatch;
    }

    public void setPauseSnatch(int pauseSnatch) {
        this.pauseSnatch = pauseSnatch;
    }

    public int getPauseClean() {
        return pauseClean;
    }

    public void setPauseClean(int pauseClean) {
        this.pauseClean = pauseClean;
    }

    public int getSandbagCarryMeters() {
        return sandbagCarryMeters;
    }

    public void setSandbagCarryMeters(int sandbagCarryMeters) {
        this.sandbagCarryMeters = sandbagCarryMeters;
    }

    public int getSandbagCarryWeight() {
        return sandbagCarryWeight;
    }

    public void setSandbagCarryWeight(int sandbagCarryWeight) {
        this.sandbagCarryWeight = sandbagCarryWeight;
    }

    public int getFarmerCarryMeters() {
        return farmerCarryMeters;
    }

    public void setFarmerCarryMeters(int farmerCarryMeters) {
        this.farmerCarryMeters = farmerCarryMeters;
    }

    public int getFarmerCarryWeight() {
        return farmerCarryWeight;
    }

    public void setFarmerCarryWeight(int farmerCarryWeight) {
        this.farmerCarryWeight = farmerCarryWeight;
    }
}
