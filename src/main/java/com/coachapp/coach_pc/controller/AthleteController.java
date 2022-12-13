package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.service.AthleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/athletes")
public class AthleteController {

    private AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @PutMapping("/{athleteId}/currentProgram/{programId}")
    public ResponseEntity<AthleteData> setCurrentProgram(@PathVariable UUID programId, @PathVariable UUID athleteId) {
        return athleteService.setCurrentProgram(athleteId, programId);
    }
}
