package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.repository.AthleteProgramStatsRepo;
import com.coachapp.coach_pc.repository.AthleteRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteProgramStatsService {

    private AthleteProgramStatsRepo statsRepo;
    private AthleteRepo athleteRepo;

    public AthleteProgramStatsService(AthleteProgramStatsRepo statsRepo, AthleteRepo athleteRepo) {
        this.statsRepo = statsRepo;
        this.athleteRepo = athleteRepo;
    }

    public ResponseEntity<AthleteProgramStats> getExistingProgramStats(UUID id) {
//        Optional<AthleteProgramStats> optional = statsRepo.findById(id);
//
//        if (optional.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        return null;
    }

    public ResponseEntity<AthleteProgramStats> generateAthleteStats(UUID athleteId) {
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        try {
            AthleteData athlete = optional.get();
            Program currentProgram = athlete.getCurrentProgram();
            AthleteProgramStats stats = statsRepo.getStats(currentProgram.getId());
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
