package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.repository.TeamRepo;
import com.coachapp.coach_pc.request.NewTeamRequest;
import com.coachapp.coach_pc.request.TeamRequest;
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

    public TeamService(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    public ResponseEntity<List<Team>> getCoachTeams(UUID coachId) {
        Optional<List<Team>> dbTeams = teamRepo.findAllByCoachId(coachId);

        if (dbTeams.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(dbTeams.get(), HttpStatus.OK);
    }

    public ResponseEntity<Team> updateTeam(UUID id, TeamRequest request) {
        boolean exists = teamRepo.existsById(id);

        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Team team = TeamRequest.convertRequest(request);

        team = teamRepo.save(team);

        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    public ResponseEntity<Team> addTeam(NewTeamRequest request) {
        Team team = NewTeamRequest.convertRequest(request);
        team = teamRepo.save(team);

        return new ResponseEntity<>(team, HttpStatus.CREATED);
    }
}
