package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.request.NewTeamRequest;
import com.coachapp.coach_pc.request.TeamRequest;
import com.coachapp.coach_pc.service.TeamService;
import com.coachapp.coach_pc.view.DisplayProgram;
import com.coachapp.coach_pc.view.DisplayTeam;
import com.coachapp.coach_pc.view.ProgramViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/teams")
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/coach/{id}")
    public ResponseEntity<List<Team>> getCoachTeams(@PathVariable UUID id) {
        return teamService.getCoachTeams(id);
    }

    @GetMapping("/")
    public ResponseEntity<List<DisplayTeam>> getDisplayTeams() {
        return teamService.getDisplayTeams();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable UUID id, @RequestBody TeamRequest team) {
        return teamService.updateTeam(id, team);
    }

    @PostMapping
    public ResponseEntity<Team> addTeam(@RequestBody NewTeamRequest team) {
        return teamService.addTeam(team);
    }
}
