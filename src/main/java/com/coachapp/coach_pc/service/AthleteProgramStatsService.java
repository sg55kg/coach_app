package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.repository.AthleteProgramStatsRepo;
import com.coachapp.coach_pc.repository.AthleteRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
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
        Optional<AthleteProgramStats> optional = statsRepo.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
    }

    public ResponseEntity<AthleteProgramStats> generateAthleteStats(UUID athleteId) {
        AthleteProgramStats stats = new AthleteProgramStats();
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = optional.get();
        int totalSnatchReps = 0;
        int totalCleanReps = 0;
        int totalJerkReps = 0;
        int totalSquatReps = 0;
        int totalPullReps = 0;
        int totalPressReps = 0;
        int totalRowReps = 0;
        int totalPRs = 0;

        for (Program program : athlete.getPrograms()) {
            for (Day day : program.getDays()) {
                for (Exercise exercise : day.getExercises()) {
                    String name = exercise.getName().toUpperCase();
                    name.contains("SNATCH");
                    switch (name) {
                        case "A":
                            System.out.println("Hello Word");
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
