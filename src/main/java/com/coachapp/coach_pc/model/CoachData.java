package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "coach_data")
public class CoachData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Program> programs;
    @OneToOne(mappedBy = "coachData")
    @JsonIgnore
    private UserData user;
    @OneToMany(mappedBy = "coach", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private List<AthleteData> athletes;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Team> teams;


    public CoachData() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
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
}
