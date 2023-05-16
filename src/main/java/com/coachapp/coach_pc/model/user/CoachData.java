package com.coachapp.coach_pc.model.user;

import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.payment.TeamFinance;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class CoachData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Program> programsList = new ArrayList<>();
    @OneToOne(mappedBy = "coachData")
    @JsonIgnore
    private UserData user;
    @OneToMany(mappedBy = "coach", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private List<AthleteData> athletes = new ArrayList<>();
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamFinance> teamFinances;

    public CoachData() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Program> getPrograms() {
        return programsList;
    }

    public void setPrograms(List<Program> programs) {
        this.programsList = programs;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public List<AthleteData> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<AthleteData> athletes) {
        this.athletes = athletes;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void addProgram(Program program) {
        this.programsList.add(program);
    }

    public List<TeamFinance> getTeamFinances() {
        return teamFinances;
    }

    public void setTeamFinances(List<TeamFinance> teamFinances) {
        this.teamFinances = teamFinances;
    }
}
