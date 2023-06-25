package com.coachapp.coach_pc.controller.user;

import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.request.user.CoachRequest;
import com.coachapp.coach_pc.request.program.ProgramRequest;
import com.coachapp.coach_pc.service.user.CoachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)
@Controller
@RequestMapping("/api/coach")
public class CoachController {

    private final CoachService coachService;
    private Logger logger = LoggerFactory.getLogger(CoachController.class);

    @Autowired
    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachData> getCoachData(@PathVariable UUID id) {
        return coachService.getCoachData(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachData> updateCoachData(@RequestBody CoachRequest data) {
        logger.info("Received request to update coach data for coach: " + data.getId());
        return coachService.updateCoachData(data);
    }

    @PutMapping("/{id}/programs")
    public ResponseEntity<CoachData> addProgram(@RequestBody ProgramRequest programRequest, @PathVariable UUID id) {
        logger.info("Received request to create a new program via coach controller");
        return coachService.addProgram(programRequest, id);
    }
}
