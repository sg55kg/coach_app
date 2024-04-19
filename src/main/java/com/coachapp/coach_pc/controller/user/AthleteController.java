package com.coachapp.coach_pc.controller.user;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.request.user.AthleteRequest;
import com.coachapp.coach_pc.request.record.AthleteRecordRequestModel;
import com.coachapp.coach_pc.service.user.AthleteService;
import com.coachapp.coach_pc.view.record.AthleteRecordViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;
import com.coachapp.coach_pc.view.user.AthleteWithPrograms;
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

    @PutMapping("/{id}")
    public ResponseEntity<AthleteViewModel> updateAthlete(@PathVariable UUID id, @RequestBody AthleteRequest athlete) {
        return athleteService.updateAthlete(id, athlete);
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<AthleteWithPrograms> getAthlete(@PathVariable UUID athleteId) {
        AthleteWithPrograms athlete = athleteService.getAthleteData(athleteId);
        if (athlete != null) {
            return new ResponseEntity<>(athlete, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{athleteId}/records")
    public ResponseEntity<List<AthleteRecordViewModel>> getAthleteRecordsByExerciseName(
            @PathVariable UUID athleteId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer reps,
            @RequestParam(required = false) Integer weight,
            @RequestParam(required = true) Boolean current
    ) {
        return athleteService.getAthleteRecordsByExerciseName(athleteId, name, reps, weight, current);
    }

    @GetMapping("/{athleteId}/records/exercise/{exerciseId}")
    public ResponseEntity<AthleteRecordViewModel> getAthleteRecordByExerciseId(@PathVariable UUID athleteId, @PathVariable UUID exerciseId) {
        return athleteService.getAthleteRecordByExerciseId(athleteId, exerciseId);
    }

    @GetMapping("/{athleteId}/records/day/{dayId}")
    public ResponseEntity<List<AthleteRecordViewModel>> getAthleteRecordByDayId(@PathVariable UUID athleteId, @PathVariable UUID dayId) {
        return athleteService.getAthleteRecordsByDayId(athleteId, dayId);
    }

    @PostMapping("/{athleteId}/records")
    public ResponseEntity<AthleteRecordViewModel> createAthleteRecord(@PathVariable UUID athleteId, @RequestBody AthleteRecordRequestModel request) {
        return athleteService.createAthleteRecord(athleteId, request);
    }

    // TODO: Remove '/list' and remove single create record post mapping above
    @PostMapping("/{athleteId}/records/list")
    public ResponseEntity<List<AthleteRecordViewModel>> createAthleteRecords(
            @PathVariable UUID athleteId, @RequestBody List<AthleteRecordRequestModel> request
    ) {
        return athleteService.createAthleteRecords(athleteId, request);
    }
}
