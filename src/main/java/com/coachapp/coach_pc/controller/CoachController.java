package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/coach")
public class CoachController {

    private final CoachService coachService;

    @Autowired
    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachData> getCoachData(@PathVariable UUID id) {
        return coachService.getCoachData(id);
    }

    @PostMapping
    public ResponseEntity<CoachData> createCoachData(@RequestBody CoachData data) {
        return coachService.createCoachData(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachData> updateCoachData(@RequestBody CoachData data) {
        return coachService.updateCoachData(data);
    }
}
