package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.service.AthleteProgramStatsService;
import com.coachapp.coach_pc.view.programStats.AthleteProgramStatsView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats/")
public class ProgramStatsController {

    private AthleteProgramStatsService service;

    public ProgramStatsController(AthleteProgramStatsService service) {
        this.service = service;
    }

    @GetMapping("{programId}")
    public ResponseEntity<AthleteProgramStatsView> getProgramStats(@PathVariable UUID programId) {
        return service.getAthleteProgramStats(programId);
    }

    @GetMapping("{programId}/weekly")
    public ResponseEntity<List<AthleteProgramStatsView>> getProgramStatsByWeek(@PathVariable UUID programId) {
        return service.getAthleteProgramStatsByWeek(programId);
    }
}
