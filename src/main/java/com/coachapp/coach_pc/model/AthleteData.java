package com.coachapp.coach_pc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "athlete_data")
public class AthleteData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @OneToOne(mappedBy = "athleteData")
    @JsonIgnore
    private UserData user;
    @OneToOne
    @JoinColumn(name = "current_program_id")
    private Program currentProgram;
    @OneToMany(mappedBy = "athlete")
    private List<Program> programs;
    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    @JsonIgnore
    private CoachData coach;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    private String name;
    @OneToOne(cascade = { CascadeType.PERSIST }, mappedBy = "athlete")
    private AthleteRecord records;

    public AthleteData() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Program getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(Program currentProgram) {
        this.currentProgram = currentProgram;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public AthleteRecord getRecords() {
        return records;
    }

    public void setRecords(AthleteRecord records) {
        this.records = records;
    }
}
