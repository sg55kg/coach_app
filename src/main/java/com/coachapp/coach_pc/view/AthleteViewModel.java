package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;

import java.util.List;
import java.util.UUID;

public class AthleteViewModel {

    private UUID id;
    private UUID coachId;
    private DisplayTeam team;
    private ProgramViewModel currentProgram;
    private String name;
    private List<AthleteRecord> records;

    public AthleteViewModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public DisplayTeam getTeam() {
        return team;
    }

    public void setTeam(DisplayTeam team) {
        this.team = team;
    }

    public ProgramViewModel getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(ProgramViewModel currentProgram) {
        this.currentProgram = currentProgram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AthleteRecord> getRecords() {
        return records;
    }

    public void setRecords(List<AthleteRecord> records) {
        this.records = records;
    }

    public static AthleteViewModel convertAthlete(AthleteData athlete) {
        AthleteViewModel viewModel = new AthleteViewModel();

        viewModel.setId(athlete.getId());
        viewModel.setCoachId(athlete.getCoach().getId());
        viewModel.setTeam(DisplayTeam.convertTeam(athlete.getTeam()));
        viewModel.setCurrentProgram(
                ProgramViewModel.convertProgram(athlete.getCurrentProgram())
        );
        viewModel.setName(athlete.getName());
        viewModel.setRecords(athlete.getRecords());

        return viewModel;
    }
}
