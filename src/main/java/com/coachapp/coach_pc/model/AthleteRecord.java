package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
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
    private OffsetDateTime lastUpdated;
    @JsonProperty("snatch")
    @JsonAlias("snatch")
    private int snatch;
    @JsonProperty("clean_and_jerk")
    @JsonAlias("clean_and_jerk")
    private int clean_and_jerk;
    @JsonProperty("jerk")
    @JsonAlias("jerk")
    private int jerk;
    @JsonProperty("clean")
    private int clean;
    @JsonProperty("back_squat")
    @JsonAlias("back_squat")
    private int back_squat;
    @JsonProperty("front_squat")
    @JsonAlias("front_squat")
    private int front_squat;
    @JsonProperty("deadlift")
    private int deadlift;
    @JsonProperty("snatch_deadlift")
    @JsonAlias("snatch_deadlift")
    private int snatch_deadlift;
    @JsonProperty("push_press")
    @JsonAlias("push_press")
    private int push_press;
    @JsonProperty("strict_press")
    @JsonAlias("strict_press")
    private int strict_press;
    @JsonProperty("bent_over_row")
    @JsonAlias("bent_over_row")
    private int bent_over_row;
    @JsonProperty("power_snatch")
    @JsonAlias("power_snatch")
    private int power_snatch;
    @JsonProperty("power_clean")
    @JsonAlias("power_clean")
    private int power_clean;
    @JsonProperty("power_jerk")
    @JsonAlias("power_jerk")
    private int power_jerk;
    @JsonProperty("hang_snatch")
    @JsonAlias("hang_snatch")
    private int hang_snatch;
    @JsonProperty("hang_power_snatch")
    private int hang_power_snatch;
    @JsonProperty("hang_power_clean")
    private int hang_power_clean;
    @JsonProperty("block_snatch")
    private int block_snatch;
    @JsonProperty("block_clean")
    private int block_clean;
    @JsonProperty("block_power_snatch")
    private int block_power_snatch;
    @JsonProperty("block_power_clean")
    private int block_power_clean;
    @JsonProperty("clean_pull")
    private int clean_pull;
    @JsonProperty("snatch_pull")
    private int snatch_pull;
    @JsonProperty("snatch_high_pull")
    private int snatch_high_pull;
    @JsonProperty("bench_press")
    private int bench_press;
    @JsonProperty("pendlay_row")
    private int pendlay_row;
    @JsonProperty("snatch_push_press")
    private int snatch_push_press;
    @JsonProperty("overhead_squat")
    private int overhead_squat;
    @JsonProperty("squat_jerk")
    private int squat_jerk;
    @JsonProperty("max_pull_ups")
    private int max_pull_ups;
    @JsonProperty("weighted_pull_up")
    private int weighted_pull_up;
    @JsonProperty("max_chin_ups")
    private int max_chin_ups;
    @JsonProperty("weighted_chin_up")
    private int weighted_chin_up;
    @JsonProperty("pause_snatch")
    private int pause_snatch;
    @JsonProperty("pause_clean")
    private int pause_clean;
    @JsonProperty("sandbag_carry_meters")
    private int sandbag_carry_meters;
    @JsonProperty("sandbag_carry_weight")
    private int sandbag_carry_weight;
    @JsonProperty("farmer_carry_meters")
    private int farmer_carry_meters;
    @JsonProperty("farmer_carry_weight")
    private int farmer_carry_weight;
    @JsonProperty("snatch_balance")
    private int snatch_balance;
    private int hang_muscle_snatch;
    private int deficit_snatch;
    private int deficit_clean;
    private int romanian_deadlift;
    private int snatch_romanian_deadlift;
    private int deficit_deadlift;
    private int block_deadlift;
    private int pause_back_squat;
    private int pause_front_squat;
    private int pause_jerk_dip;
    private int pause_jerk_catch;
    private int muscle_snatch;
    private int low_hang_snatch;
    private int low_hang_clean;
    private int high_hang_snatch;
    private int high_hang_clean;
    private int sumo_deadlift;
    private int pause_deadlift;


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

    public Date getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getSnatch() {
        return snatch;
    }

    public void setSnatch(int snatch) {
        this.snatch = snatch;
    }

    public int getClean_and_jerk() {
        return clean_and_jerk;
    }

    public void setClean_and_jerk(int clean_and_jerk) {
        this.clean_and_jerk = clean_and_jerk;
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

    public int getBack_squat() {
        return back_squat;
    }

    public void setBack_squat(int back_squat) {
        this.back_squat = back_squat;
    }

    public int getFront_squat() {
        return front_squat;
    }

    public void setFront_squat(int front_squat) {
        this.front_squat = front_squat;
    }

    public int getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(int deadlift) {
        this.deadlift = deadlift;
    }

    public int getSnatch_deadlift() {
        return snatch_deadlift;
    }

    public void setSnatch_deadlift(int snatch_deadlift) {
        this.snatch_deadlift = snatch_deadlift;
    }

    public int getPush_press() {
        return push_press;
    }

    public void setPush_press(int push_press) {
        this.push_press = push_press;
    }

    public int getStrict_press() {
        return strict_press;
    }

    public void setStrict_press(int strict_press) {
        this.strict_press = strict_press;
    }

    public int getBent_over_row() {
        return bent_over_row;
    }

    public void setBent_over_row(int bent_over_row) {
        this.bent_over_row = bent_over_row;
    }

    public int getPower_snatch() {
        return power_snatch;
    }

    public void setPower_snatch(int power_snatch) {
        this.power_snatch = power_snatch;
    }

    public int getPower_clean() {
        return power_clean;
    }

    public void setPower_clean(int power_clean) {
        this.power_clean = power_clean;
    }

    public int getPower_jerk() {
        return power_jerk;
    }

    public void setPower_jerk(int power_jerk) {
        this.power_jerk = power_jerk;
    }

    public int getHang_snatch() {
        return hang_snatch;
    }

    public void setHang_snatch(int hang_snatch) {
        this.hang_snatch = hang_snatch;
    }

    public int getHang_power_snatch() {
        return hang_power_snatch;
    }

    public void setHang_power_snatch(int hang_power_snatch) {
        this.hang_power_snatch = hang_power_snatch;
    }

    public int getHang_power_clean() {
        return hang_power_clean;
    }

    public void setHang_power_clean(int hang_power_clean) {
        this.hang_power_clean = hang_power_clean;
    }

    public int getBlock_snatch() {
        return block_snatch;
    }

    public void setBlock_snatch(int block_snatch) {
        this.block_snatch = block_snatch;
    }

    public int getBlock_clean() {
        return block_clean;
    }

    public void setBlock_clean(int block_clean) {
        this.block_clean = block_clean;
    }

    public int getBlock_power_snatch() {
        return block_power_snatch;
    }

    public void setBlock_power_snatch(int block_power_snatch) {
        this.block_power_snatch = block_power_snatch;
    }

    public int getBlock_power_clean() {
        return block_power_clean;
    }

    public void setBlock_power_clean(int block_power_clean) {
        this.block_power_clean = block_power_clean;
    }

    public int getClean_pull() {
        return clean_pull;
    }

    public void setClean_pull(int clean_pull) {
        this.clean_pull = clean_pull;
    }

    public int getSnatch_pull() {
        return snatch_pull;
    }

    public void setSnatch_pull(int snatch_pull) {
        this.snatch_pull = snatch_pull;
    }

    public int getSnatch_high_pull() {
        return snatch_high_pull;
    }

    public void setSnatch_high_pull(int snatch_high_pull) {
        this.snatch_high_pull = snatch_high_pull;
    }

    public int getBench_press() {
        return bench_press;
    }

    public void setBench_press(int bench_press) {
        this.bench_press = bench_press;
    }

    public int getPendlay_row() {
        return pendlay_row;
    }

    public void setPendlay_row(int pendlay_row) {
        this.pendlay_row = pendlay_row;
    }

    public int getSnatch_push_press() {
        return snatch_push_press;
    }

    public void setSnatch_push_press(int snatch_push_press) {
        this.snatch_push_press = snatch_push_press;
    }

    public int getOverhead_squat() {
        return overhead_squat;
    }

    public void setOverhead_squat(int overhead_squat) {
        this.overhead_squat = overhead_squat;
    }

    public int getSquat_jerk() {
        return squat_jerk;
    }

    public void setSquat_jerk(int squat_jerk) {
        this.squat_jerk = squat_jerk;
    }

    public int getMax_pull_ups() {
        return max_pull_ups;
    }

    public void setMax_pull_ups(int max_pull_ups) {
        this.max_pull_ups = max_pull_ups;
    }

    public int getWeighted_pull_up() {
        return weighted_pull_up;
    }

    public void setWeighted_pull_up(int weighted_pull_up) {
        this.weighted_pull_up = weighted_pull_up;
    }

    public int getMax_chin_ups() {
        return max_chin_ups;
    }

    public void setMax_chin_ups(int max_chin_ups) {
        this.max_chin_ups = max_chin_ups;
    }

    public int getWeighted_chin_up() {
        return weighted_chin_up;
    }

    public void setWeighted_chin_up(int weighted_chin_up) {
        this.weighted_chin_up = weighted_chin_up;
    }

    public int getPause_snatch() {
        return pause_snatch;
    }

    public void setPause_snatch(int pause_snatch) {
        this.pause_snatch = pause_snatch;
    }

    public int getPause_clean() {
        return pause_clean;
    }

    public void setPause_clean(int pause_clean) {
        this.pause_clean = pause_clean;
    }

    public int getSandbag_carry_meters() {
        return sandbag_carry_meters;
    }

    public void setSandbag_carry_meters(int sandbag_carry_meters) {
        this.sandbag_carry_meters = sandbag_carry_meters;
    }

    public int getSandbag_carry_weight() {
        return sandbag_carry_weight;
    }

    public void setSandbag_carry_weight(int sandbag_carry_weight) {
        this.sandbag_carry_weight = sandbag_carry_weight;
    }

    public int getFarmer_carry_meters() {
        return farmer_carry_meters;
    }

    public void setFarmer_carry_meters(int farmer_carry_meters) {
        this.farmer_carry_meters = farmer_carry_meters;
    }

    public int getFarmer_carry_weight() {
        return farmer_carry_weight;
    }

    public void setFarmer_carry_weight(int farmer_carry_weight) {
        this.farmer_carry_weight = farmer_carry_weight;
    }

    public int getSnatch_balance() {
        return snatch_balance;
    }

    public void setSnatch_balance(int snatch_balance) {
        this.snatch_balance = snatch_balance;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getHang_muscle_snatch() {
        return hang_muscle_snatch;
    }

    public void setHang_muscle_snatch(int hang_muscle_snatch) {
        this.hang_muscle_snatch = hang_muscle_snatch;
    }

    public int getDeficit_snatch() {
        return deficit_snatch;
    }

    public void setDeficit_snatch(int deficit_snatch) {
        this.deficit_snatch = deficit_snatch;
    }

    public int getDeficit_clean() {
        return deficit_clean;
    }

    public void setDeficit_clean(int deficit_clean) {
        this.deficit_clean = deficit_clean;
    }

    public int getRomanian_deadlift() {
        return romanian_deadlift;
    }

    public void setRomanian_deadlift(int romanian_deadlift) {
        this.romanian_deadlift = romanian_deadlift;
    }

    public int getSnatch_romanian_deadlift() {
        return snatch_romanian_deadlift;
    }

    public void setSnatch_romanian_deadlift(int snatch_romanian_deadlift) {
        this.snatch_romanian_deadlift = snatch_romanian_deadlift;
    }

    public int getDeficit_deadlift() {
        return deficit_deadlift;
    }

    public void setDeficit_deadlift(int deficit_deadlift) {
        this.deficit_deadlift = deficit_deadlift;
    }

    public int getBlock_deadlift() {
        return block_deadlift;
    }

    public void setBlock_deadlift(int block_deadlift) {
        this.block_deadlift = block_deadlift;
    }

    public int getPause_back_squat() {
        return pause_back_squat;
    }

    public void setPause_back_squat(int pause_back_squat) {
        this.pause_back_squat = pause_back_squat;
    }

    public int getPause_front_squat() {
        return pause_front_squat;
    }

    public void setPause_front_squat(int pause_front_squat) {
        this.pause_front_squat = pause_front_squat;
    }

    public int getPause_jerk_dip() {
        return pause_jerk_dip;
    }

    public void setPause_jerk_dip(int pause_jerk_dip) {
        this.pause_jerk_dip = pause_jerk_dip;
    }

    public int getPause_jerk_catch() {
        return pause_jerk_catch;
    }

    public void setPause_jerk_catch(int pause_jerk_catch) {
        this.pause_jerk_catch = pause_jerk_catch;
    }

    public int getMuscle_snatch() {
        return muscle_snatch;
    }

    public void setMuscle_snatch(int muscle_snatch) {
        this.muscle_snatch = muscle_snatch;
    }

    public int getLow_hang_snatch() {
        return low_hang_snatch;
    }

    public void setLow_hang_snatch(int low_hang_snatch) {
        this.low_hang_snatch = low_hang_snatch;
    }

    public int getLow_hang_clean() {
        return low_hang_clean;
    }

    public void setLow_hang_clean(int low_hang_clean) {
        this.low_hang_clean = low_hang_clean;
    }

    public int getHigh_hang_snatch() {
        return high_hang_snatch;
    }

    public void setHigh_hang_snatch(int high_hang_snatch) {
        this.high_hang_snatch = high_hang_snatch;
    }

    public int getHigh_hang_clean() {
        return high_hang_clean;
    }

    public void setHigh_hang_clean(int high_hang_clean) {
        this.high_hang_clean = high_hang_clean;
    }

    public int getSumo_deadlift() {
        return sumo_deadlift;
    }

    public void setSumo_deadlift(int sumo_deadlift) {
        this.sumo_deadlift = sumo_deadlift;
    }

    public int getPause_deadlift() {
        return pause_deadlift;
    }

    public void setPause_deadlift(int pause_deadlift) {
        this.pause_deadlift = pause_deadlift;
    }

    public void copyValues(AthleteRecord values) {
        this.snatch = values.getSnatch();
        this.clean_and_jerk = values.getClean_and_jerk();
        this.jerk = values.getJerk();
        this.clean = values.getClean();
        this.back_squat = values.getBack_squat();
        this.front_squat = values.getFront_squat();
        this.bench_press = values.getBench_press();
        this.bent_over_row = values.getBent_over_row();
        this.block_clean = values.getBlock_clean();
        this.deadlift = values.getDeadlift();
        this.block_power_clean = values.getBlock_power_clean();
        this.block_power_snatch = values.getBlock_power_snatch();
        this.block_snatch = values.getBlock_snatch();
        this.clean_pull = values.getClean_pull();
        this.farmer_carry_meters = values.getFarmer_carry_meters();
        this.farmer_carry_weight = values.getFarmer_carry_weight();
        this.hang_power_clean = values.getHang_power_clean();
        this.hang_power_snatch = values.getHang_power_snatch();
        this.hang_snatch = values.getHang_snatch();
        this.max_chin_ups = values.getMax_chin_ups();
        this.max_pull_ups = values.getMax_pull_ups();
        this.overhead_squat = values.getOverhead_squat();
        this.push_press = values.getPush_press();
        this.pause_clean = values.getPause_clean();
        this.pause_snatch = values.getPause_snatch();
        this.pendlay_row = values.getPendlay_row();
        this.sandbag_carry_meters = values.getSandbag_carry_meters();
        this.sandbag_carry_weight = values.getSandbag_carry_weight();
        this.squat_jerk = values.getSquat_jerk();
        this.snatch_balance = values.getSnatch_balance();
        this.weighted_chin_up = values.getWeighted_chin_up();
        this.weighted_pull_up = values.getWeighted_pull_up();
        this.strict_press = values.getStrict_press();
        this.snatch_pull = values.getSnatch_pull();
        this.snatch_high_pull = values.getSnatch_high_pull();
        this.snatch_push_press = values.getSnatch_push_press();
        this.power_clean = values.getPower_clean();
        this.power_jerk = values.getPower_jerk();
        this.power_snatch = values.getPower_snatch();
        this.snatch_deadlift = values.getSnatch_deadlift();
        this.block_deadlift = values.getBlock_deadlift();
        this.deficit_clean = values.getDeficit_clean();
        this.deficit_snatch = values.getDeficit_snatch();
        this.deficit_deadlift = values.getDeficit_deadlift();
        this.romanian_deadlift = values.getRomanian_deadlift();
        this.snatch_romanian_deadlift = values.getSnatch_romanian_deadlift();
        this.hang_muscle_snatch = values.getHang_muscle_snatch();
        this.muscle_snatch = values.getMuscle_snatch();
        this.pause_jerk_dip = values.getPause_jerk_dip();
        this.pause_jerk_catch = values.getPause_jerk_catch();
        this.pause_deadlift = values.getPause_deadlift();
        this.sumo_deadlift = values.getSumo_deadlift();
        this.low_hang_snatch = values.getLow_hang_snatch();
        this.high_hang_snatch = values.getHigh_hang_snatch();
        this.low_hang_clean = values.getLow_hang_clean();
        this.high_hang_clean = values.getHigh_hang_clean();

    }
}
