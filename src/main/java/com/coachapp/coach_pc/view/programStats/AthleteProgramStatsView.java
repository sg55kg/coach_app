package com.coachapp.coach_pc.view.programStats;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AthleteProgramStatsView {
    private UUID programId;
    private String programName;
    private double plannedTotalVolume;
    private int plannedTotalReps;
    private double plannedAverageIntensity;
    private double plannedKValue;
    private double plannedInol;
    private double actualTotalVolume;
    private double actualTotalReps;
    private double actualAverageIntensity;
    private double actualKValue;
    private double actualInol;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    public AthleteProgramStatsView() {}

    public UUID getProgramId() {
        return programId;
    }

    public void setProgramId(UUID programId) {
        this.programId = programId;
    }

    public double getPlannedTotalVolume() {
        return plannedTotalVolume;
    }

    public void setPlannedTotalVolume(double plannedTotalVolume) {
        this.plannedTotalVolume = plannedTotalVolume;
    }

    public int getPlannedTotalReps() {
        return plannedTotalReps;
    }

    public void setPlannedTotalReps(int plannedTotalReps) {
        this.plannedTotalReps = plannedTotalReps;
    }

    public double getPlannedAverageIntensity() {
        return plannedAverageIntensity;
    }

    public void setPlannedAverageIntensity(double plannedAverageIntensity) {
        this.plannedAverageIntensity = plannedAverageIntensity;
    }

    public double getPlannedKValue() {
        return plannedKValue;
    }

    public void setPlannedKValue(double plannedKValue) {
        this.plannedKValue = plannedKValue;
    }

    public double getPlannedInol() {
        return plannedInol;
    }

    public void setPlannedInol(double plannedInol) {
        this.plannedInol = plannedInol;
    }

    public double getActualTotalVolume() {
        return actualTotalVolume;
    }

    public void setActualTotalVolume(double actualTotalVolume) {
        this.actualTotalVolume = actualTotalVolume;
    }

    public double getActualTotalReps() {
        return actualTotalReps;
    }

    public void setActualTotalReps(double actualTotalReps) {
        this.actualTotalReps = actualTotalReps;
    }

    public double getActualAverageIntensity() {
        return actualAverageIntensity;
    }

    public void setActualAverageIntensity(double actualAverageIntensity) {
        this.actualAverageIntensity = actualAverageIntensity;
    }

    public double getActualKValue() {
        return actualKValue;
    }

    public void setActualKValue(double actualKValue) {
        this.actualKValue = actualKValue;
    }

    public double getActualInol() {
        return actualInol;
    }

    public void setActualInol(double actualInol) {
        this.actualInol = actualInol;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }
}
