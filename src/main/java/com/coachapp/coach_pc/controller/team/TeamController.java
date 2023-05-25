package com.coachapp.coach_pc.controller.team;

import com.coachapp.coach_pc.request.team.NewTeamRequest;
import com.coachapp.coach_pc.request.team.TeamRequest;
import com.coachapp.coach_pc.service.team.TeamService;
import com.coachapp.coach_pc.view.team.DisplayTeam;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.team.TeamWithAthletes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/teams")
public class TeamController {

    private TeamService teamService;
    private Logger logger = LoggerFactory.getLogger(TeamController.class);

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/coach/{id}")
    public ResponseEntity<List<TeamWithAthletes>> getCoachTeams(@PathVariable UUID id) {
        return teamService.getCoachTeams(id);
    }

    @GetMapping("/")
    public ResponseEntity<List<DisplayTeam>> getDisplayTeams() {
        return teamService.getDisplayTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamWithAthletes> getTeam(@PathVariable UUID id) {
        return teamService.getTeam(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamViewModel> updateTeam(@PathVariable UUID id, @RequestBody TeamRequest team) {
        logger.info("Received request to update team: " + team.getId());
        return teamService.updateTeam(id, team);
    }

    @PostMapping
    public ResponseEntity<TeamViewModel> addTeam(@RequestBody NewTeamRequest team) {
        logger.info("Received request to create a new team");
        return teamService.addTeam(team);
    }
}
