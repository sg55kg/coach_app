package com.coachapp.coach_pc.controller.team;

import com.coachapp.coach_pc.request.team.NewTeamRequest;
import com.coachapp.coach_pc.request.team.TeamRequest;
import com.coachapp.coach_pc.service.team.TeamService;
import com.coachapp.coach_pc.view.team.DisplayTeam;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.team.TeamWithAthletes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)

// Add logs to every controller route indicating request params (masking private data) and response
@Controller
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

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
        logger.info("Received request to get team");
        try {
            return teamService.getTeam(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not retrieve team");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/{id}/display")
    public ResponseEntity<DisplayTeam> getDisplayTeam(@PathVariable UUID id) {
        return teamService.getDisplayTeam(id);
    }
}
