package com.coachapp.coach_pc.service.team;

import com.coachapp.coach_pc.repository.team.TeamRepository;
import com.coachapp.coach_pc.request.team.NewTeamRequest;
import com.coachapp.coach_pc.request.team.TeamRequest;
import com.coachapp.coach_pc.view.team.DisplayTeam;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.team.TeamWithAthletes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Add logs to every service method indicating request params (masking private data) and response values for debugging
@Service
public class TeamService {

    private TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<TeamWithAthletes>> getCoachTeams(UUID coachId) {
        List<TeamWithAthletes> teams = repository.findAllByCoachId(coachId);

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    public ResponseEntity<TeamViewModel> updateTeam(UUID id, TeamRequest request) {
        TeamViewModel vm = repository.updateTeam(request);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    public ResponseEntity<TeamViewModel> addTeam(NewTeamRequest request) {
        TeamViewModel team = repository.createTeam(request);

        return new ResponseEntity<>(team, HttpStatus.CREATED);
    }

    public ResponseEntity<List<DisplayTeam>> getDisplayTeams() {
        List<DisplayTeam> teams = repository.getDisplayTeams();

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    public ResponseEntity<TeamWithAthletes> getTeam(UUID id) {
        Optional<TeamWithAthletes> optional = repository.findById(id);

        return optional.map(teamWithAthletes -> new ResponseEntity<>(teamWithAthletes, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<DisplayTeam> getDisplayTeam(UUID teamId) {
        Optional<DisplayTeam> optional = repository.findDisplayById(teamId);

        return optional.map(displayTeam -> new ResponseEntity<>(displayTeam, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
