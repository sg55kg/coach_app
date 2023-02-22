package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.CoachData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoachViewModel {

    private UUID id;
    private List<AthleteViewModel> athletes;
    private List<TeamViewModel> teams;
    private List<DisplayProgram> programs;

    public CoachViewModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<AthleteViewModel> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<AthleteViewModel> athletes) {
        this.athletes = athletes;
    }

    public List<TeamViewModel> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamViewModel> teams) {
        this.teams = teams;
    }

    public List<DisplayProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<DisplayProgram> programs) {
        this.programs = programs;
    }

    public static CoachViewModel convertCoach(CoachData coach) {
        CoachViewModel viewModel = new CoachViewModel();
        List<TeamViewModel> teams = new ArrayList<>();
        List<AthleteViewModel> athletes = new ArrayList<>();
        List<DisplayProgram> programs = new ArrayList<>();

        coach.getTeams().forEach(t -> teams.add(TeamViewModel.convertTeam(t)));
        coach.getAthletes().forEach(a -> athletes.add(AthleteViewModel.convertAthlete(a)));
        coach.getPrograms().forEach(p -> programs.add(DisplayProgram.convertProgramForDisplay(p)));

        viewModel.setId(coach.getId());
        viewModel.setTeams(teams);
        viewModel.setAthletes(athletes);
        viewModel.setPrograms(programs);

        return viewModel;
    }
}
