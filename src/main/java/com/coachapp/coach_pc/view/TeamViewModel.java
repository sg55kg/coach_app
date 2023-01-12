package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Team;

import java.util.*;

public class TeamViewModel {

    private UUID id;
    private List<AthleteData> athletes;
    private CoachData coach;
    private String name;
    private String description;
    private Date createdAt;

    public TeamViewModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<AthleteData> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<AthleteData> athletes) {
        this.athletes = athletes;
    }

    public CoachData getCoach() {
        return coach;
    }

    public void setCoach(CoachData coach) {
        this.coach = coach;
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

    public static TeamViewModel convertTeam(Team team) {
        TeamViewModel viewModel = new TeamViewModel();

        viewModel.setId(team.getId());
        viewModel.setName(team.getName());
        viewModel.setDescription(team.getDescription());

        // prevent infinite loops in nested coach/athlete data
        List<AthleteData> viewModelAthletes = new ArrayList<>();
        team.getAthletes().forEach(a -> {
            a.setCoach(null);
            a.setTeam(null);
            viewModelAthletes.add(a);
        });
        viewModel.setAthletes(viewModelAthletes);

        CoachData viewModelCoach = team.getCoach();
        viewModelCoach.setUser(null);
        viewModelCoach.setTeams(null);
        viewModelCoach.setAthletes(Collections.emptyList());
        viewModel.setCoach(viewModelCoach);

        return viewModel;
    }
}
