package com.coachapp.coach_pc.controller.program_stat;

import com.coachapp.coach_pc.service.program_stat.AthleteProgramStatsService;
import com.coachapp.coach_pc.view.programStats.AthleteProgramStatsView;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
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

    @GetMapping("{programId}/daily")
    public ResponseEntity<List<AthleteProgramStatsView>> getProgramStatsByDay(
            @PathVariable UUID programId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate
    ) {
        return service.getAthleteProgramStatsByDay(programId, startDate, endDate);
    }
}
