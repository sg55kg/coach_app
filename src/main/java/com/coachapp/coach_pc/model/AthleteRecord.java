package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "athlete_record")
public class AthleteRecord {
    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "id")
    @JsonIgnore
    private AthleteData athlete;
    @CreationTimestamp
    private Date createdAt;
    private String lastUpdated;
    @JsonAlias("snatch")
    private int snatch;
    @JsonAlias("clean_and_jerk")
    private int cleanAndJerk;
    @JsonAlias("jerk")
    private int jerk;
    @JsonAlias("clean")
    private int clean;
    @JsonAlias("back_squat")
    private int backSquat;
    @JsonAlias("front_squat")
    private int frontSquat;
    @JsonAlias("deadlift")
    private int deadlift;
    @JsonAlias("snatch_deadlift")
    private int snatchDeadlift;
    @JsonAlias("push_press")
    private int pushPress;
    @JsonAlias("strict_press")
    private int strictPress;
    @JsonAlias("bent_over_row")
    private int bentOverRow;
    @JsonAlias("power_snatch")
    private int powerSnatch;
    @JsonAlias("power_clean")
    private int powerClean;
    @JsonAlias("power_jerk")
    private int powerJerk;
    @JsonAlias("hang_snatch")
    private int hangSnatch;
    @JsonAlias("hang_power_snatch")
    private int hangPowerSnatch;
    @JsonAlias("hang_power_clean")
    private int hangPowerClean;
    @JsonAlias("block_snatch")
    private int blockSnatch;
    @JsonAlias("block_clean")
    private int blockClean;
    @JsonAlias("block_power_snatch")
    private int blockPowerSnatch;
    @JsonAlias("block_power_clean")
    private int blockPowerClean;
    @JsonAlias("clean_pull")
    private int cleanPull;
    @JsonAlias("snatch_pull")
    private int snatchPull;
    @JsonAlias("snatch_high_pull")
    private int snatchHighPull;
    @JsonAlias("bench_press")
    private int benchPress;
    @JsonAlias("pendlay_row")
    private int pendlayRow;
    @JsonAlias("snatch_push_press")
    private int snatchPushPress;
    @JsonAlias("overhead_squat")
    private int overheadSquat;
    @JsonAlias("squat_jerk")
    private int squatJerk;
    @JsonAlias("max_pull_ups")
    private int maxPullUps;
    @JsonAlias("weighted_pull_up")
    private int weightedPullUp;
    @JsonAlias("max_chin_ups")
    private int maxChinUps;
    @JsonAlias("weighted_chin_up")
    private int weightedChinUp;
    @JsonAlias("pause_snatch")
    private int pauseSnatch;
    @JsonAlias("pause_clean")
    private int pauseClean;
    @JsonAlias("sandbag_carry_meters")
    private int sandbagCarryMeters;
    @JsonAlias("sandbag_carry_weight")
    private int sandbagCarryWeight;
    @JsonAlias("farmer_carry_meters")
    private int farmerCarryMeters;
    @JsonAlias("farmer_carry_weight")
    private int farmerCarryWeight;
    @JsonAlias("snatch_balance")
    private int snatchBalance;

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

    public int getSnatchBalance() {
        return snatchBalance;
    }

    public void setSnatchBalance(int snatchBalance) {
        this.snatchBalance = snatchBalance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
