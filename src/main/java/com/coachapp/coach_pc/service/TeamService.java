package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.repository.AthleteRepo;
import com.coachapp.coach_pc.repository.CoachRepo;
import com.coachapp.coach_pc.repository.TeamRepo;
import com.coachapp.coach_pc.request.NewTeamRequest;
import com.coachapp.coach_pc.request.TeamRequest;
import com.coachapp.coach_pc.view.DisplayTeam;
import com.coachapp.coach_pc.view.TeamViewModel;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamService {

    private TeamRepo teamRepo;
    private AthleteRepo athleteRepo;
    private CoachRepo coachRepo;

    public TeamService(TeamRepo teamRepo, AthleteRepo athleteRepo, CoachRepo coachRepo) {
        this.teamRepo = teamRepo;
        this.athleteRepo = athleteRepo;
        this.coachRepo = coachRepo;
    }

    public ResponseEntity<List<Team>> getCoachTeams(UUID coachId) {
        Optional<List<Team>> dbTeams = teamRepo.findAllByCoachId(coachId);

        if (dbTeams.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(dbTeams.get(), HttpStatus.OK);
    }

    public ResponseEntity<Team> updateTeam(UUID id, TeamRequest request) {
        Optional<Team> optional = teamRepo.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Team team = optional.get();
        team = TeamRequest.convertRequest(team, request);

        team = teamRepo.save(team);

        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    public ResponseEntity<Team> addTeam(NewTeamRequest request) {
        Team team = new Team();
        team.setTeamLogo(request.getTeamLogo());
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team = teamRepo.save(team);

        return new ResponseEntity<>(team, HttpStatus.CREATED);
    }

    public ResponseEntity<List<DisplayTeam>> getDisplayTeams() {
        List<Team> dbTeams = teamRepo.findAll();
        List<DisplayTeam> teams = new ArrayList<>();

        dbTeams.forEach(team -> {
            teams.add(DisplayTeam.convertTeam(team));
        });

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    public ResponseEntity<TeamViewModel> getTeam(UUID id) {
        Optional<Team> optional = teamRepo.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Team team = optional.get();
        TeamViewModel vm = TeamViewModel.convertTeam(team);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }
}
