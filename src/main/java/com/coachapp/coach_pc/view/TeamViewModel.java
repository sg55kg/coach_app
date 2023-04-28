package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.user.UserData;

import java.util.*;

public class TeamViewModel {

    private UUID id;
    private List<AthleteViewModel> athletes;
    private UUID coachId;
    private String coachName;
    private String name;
    private String description;
    private Date createdAt;
    private String teamLogo;
    private int numAthletes;

    public TeamViewModel() {}

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

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public int getNumAthletes() {
        return numAthletes;
    }

    public void setNumAthletes(int numAthletes) {
        this.numAthletes = numAthletes;
    }

    public static TeamViewModel convertTeam(Team team) {
        TeamViewModel viewModel = new TeamViewModel();

        viewModel.setId(team.getId());
        viewModel.setName(team.getName());
        viewModel.setDescription(team.getDescription());
        viewModel.setTeamLogo(team.getTeamLogo());
        List<AthleteData> athletes = team.getAthletes();
        viewModel.setNumAthletes(athletes.size());

        List<AthleteViewModel> viewModelAthletes = new ArrayList<>();
        athletes.forEach(a -> {
            viewModelAthletes.add(AthleteViewModel.convertAthlete(a));
        });
        viewModel.setAthletes(viewModelAthletes);

        viewModel.setCoachId(team.getCoach().getId());
        UserData coachUser = team.getCoach().getUser();
        viewModel.setCoachName(coachUser.getUsername());

        return viewModel;
    }
}
