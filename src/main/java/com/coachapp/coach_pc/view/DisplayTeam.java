package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.Team;

import java.util.UUID;

public class DisplayTeam {

    private UUID id;
    private String name;
    private String description;
    private String coachName;
    private UUID coachId;
    private int numAthletes;

    public DisplayTeam() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public int getNumAthletes() {
        return numAthletes;
    }

    public void setNumAthletes(int numAthletes) {
        this.numAthletes = numAthletes;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public static DisplayTeam convertTeam(Team team) {
        DisplayTeam displayTeam = new DisplayTeam();

        displayTeam.setCoachName(team.getCoach().getUser().getUsername());
        displayTeam.setDescription(team.getDescription());
        displayTeam.setId(team.getId());
        displayTeam.setName(team.getName());
        displayTeam.setNumAthletes(team.getAthletes().size());
        displayTeam.setCoachId(team.getCoach().getId());

        return displayTeam;
    }
}
