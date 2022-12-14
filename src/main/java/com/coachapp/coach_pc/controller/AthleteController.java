package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.request.AthleteRequest;
import com.coachapp.coach_pc.service.AthleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/athletes")
public class AthleteController {

    private AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    // This is probably unnecessary with the general PUT request below
    @PutMapping("/{athleteId}/currentProgram/{programId}")
    public ResponseEntity<AthleteData> setCurrentProgram(@PathVariable UUID programId, @PathVariable UUID athleteId) {
        return athleteService.setCurrentProgram(athleteId, programId);
    }

    @PutMapping("{id}")
    public ResponseEntity<AthleteData> updateAthlete(@PathVariable UUID id, @RequestBody AthleteRequest athlete) {
        return athleteService.updateAthlete(id, athlete);
    }

    @PutMapping("/{athleteId}/record")
    public ResponseEntity<List<AthleteRecord>> updateAthleteRecord(@PathVariable UUID athleteId, @RequestBody AthleteRecord record) {
        return athleteService.updateAthleteRecord(athleteId, record);
    }
}
