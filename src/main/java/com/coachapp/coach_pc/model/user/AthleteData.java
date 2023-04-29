package com.coachapp.coach_pc.model.user;

import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
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
    @OneToMany(mappedBy = "athlete", fetch = FetchType.LAZY)
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    private List<Program> programsList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    @JsonIgnore
    private CoachData coach;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    private String name;
//    @OneToMany(mappedBy = "athlete", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<AthleteRecord> records;

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
        return programsList;
    }

    public void setPrograms(List<Program> programs) {
        this.programsList = programs;
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

//    public List<AthleteRecord> getRecords() {
//        return records;
//    }
//
//    public void setRecords(List<AthleteRecord> records) {
//        this.records = records;
//    }
//
//    public void addRecord(AthleteRecord record) {
//        this.records.add(record);
//    }

    public void addProgram(Program program) {
        this.programsList.add(program);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AthleteData that = (AthleteData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
