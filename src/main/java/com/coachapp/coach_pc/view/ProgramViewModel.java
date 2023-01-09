package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;

import java.util.*;

public class ProgramViewModel {

    private UUID id;
    private String name;
    private Date startDate;
    private Date endDate;
    private List<DayViewModel> days;
    private UUID athleteId;
    private UUID coachId;
    private UUID teamId;

    public ProgramViewModel() {

    }

    public ProgramViewModel(UUID id, String name, Date startDate, Date endDate, List<DayViewModel> days, UUID athleteId, UUID coachId, UUID teamId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.athleteId = athleteId;
        this.coachId = coachId;
        this.teamId = teamId;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<DayViewModel> getDays() {
        return days;
    }

    public void setDays(List<DayViewModel> days) {
        this.days = days;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(UUID athleteId) {
        this.athleteId = athleteId;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public static ProgramViewModel convertProgram(Program program) {
        ProgramViewModel viewModel = new ProgramViewModel();

        viewModel.setId(program.getId());
        List<DayViewModel> set = new ArrayList<>();
        program.getDays().forEach(d ->
                set.add(new DayViewModel(
                        d.getId(),
                        d.getDate(),
                        d.getExercises(),
                        d.getIsRestDay()
                ))
        );
        viewModel.setDays(set);
        viewModel.setStartDate(program.getStartDate());
        viewModel.setEndDate(program.getEndDate());
        viewModel.setName(program.getName());
        if (program.getCoach() != null) {
            viewModel.setCoachId(program.getCoach().getId());
        }
        if (program.getAthlete() != null) {
            viewModel.setAthleteId(program.getAthlete().getId());
        }
        if (program.getTeam() != null) {
            viewModel.setTeamId(program.getTeam().getId());
        }

        return viewModel;
    }
}
