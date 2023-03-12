package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteProgramStats;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.request.AthleteRequest;
import com.coachapp.coach_pc.service.AthleteProgramStatsService;
import com.coachapp.coach_pc.service.AthleteService;
import com.coachapp.coach_pc.view.AthleteViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/athletes")
public class AthleteController {

    private AthleteService athleteService;
    private AthleteProgramStatsService statsService;

    public AthleteController(AthleteService athleteService, AthleteProgramStatsService statsService) {
        this.athleteService = athleteService;
        this.statsService = statsService;
    }

    // This is probably unnecessary with the general PUT request below
    @PutMapping("/{athleteId}/currentProgram/{programId}")
    public ResponseEntity<AthleteData> setCurrentProgram(@PathVariable UUID programId, @PathVariable UUID athleteId) {
        return athleteService.setCurrentProgram(athleteId, programId);
    }

    @PutMapping("{id}")
    public ResponseEntity<AthleteViewModel> updateAthlete(@PathVariable UUID id, @RequestBody AthleteRequest athlete) {
        return athleteService.updateAthlete(id, athlete);
    }

    @PutMapping("/{athleteId}/record")
    public ResponseEntity<List<AthleteRecord>> updateAthleteRecord(@PathVariable UUID athleteId, @RequestBody AthleteRecord record) {
        return athleteService.updateAthleteRecord(athleteId, record);
    }

    @GetMapping("{athleteId}")
    public ResponseEntity<AthleteData> getAthlete(@PathVariable UUID athleteId) {
        AthleteData athlete = athleteService.getAthleteData(athleteId);
        if (athlete != null) {
            return new ResponseEntity<>(athlete, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("{athleteId}/record")
    public ResponseEntity<List<AthleteRecord>> getAthleteRecords(@PathVariable UUID athleteId) {
        return athleteService.getAthleteRecords(athleteId);
    }

    @GetMapping("{athleteId}/stats")
    public ResponseEntity<AthleteProgramStats> getAthleteStats(@PathVariable UUID athleteId) {
        return statsService.generateAthleteStats(athleteId);
    }
}
