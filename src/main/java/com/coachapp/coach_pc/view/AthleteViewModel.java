package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.user.AthleteData;

import java.util.UUID;

public class AthleteViewModel {

    private UUID id;
    private UUID coachId;
    private DisplayTeam team;
    private ProgramViewModel currentProgram;
    private String name;


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


    public static AthleteViewModel convertAthlete(AthleteData athlete) {
        if (athlete == null) {
            return null;
        }
        AthleteViewModel viewModel = new AthleteViewModel();

        viewModel.setId(athlete.getId());
        if (athlete.getCoach() != null) {
            viewModel.setCoachId(athlete.getCoach().getId());
        }
        if (athlete.getTeam() != null) {
            viewModel.setTeam(DisplayTeam.convertTeam(athlete.getTeam()));
        }
        if (athlete.getCurrentProgram() != null) {
            viewModel.setCurrentProgram(
                    ProgramViewModel.convertProgram(athlete.getCurrentProgram())
            );
        }

        viewModel.setName(athlete.getName());

        return viewModel;
    }
}
