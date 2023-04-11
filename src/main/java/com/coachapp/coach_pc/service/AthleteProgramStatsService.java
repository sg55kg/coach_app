package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.repository.AthleteProgramStatsRepository;
import com.coachapp.coach_pc.view.programStats.AthleteProgramStatsView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AthleteProgramStatsService {

    private AthleteProgramStatsRepository repository;

    public AthleteProgramStatsService(AthleteProgramStatsRepository repository) {
        this.repository = repository;
    }
    public ResponseEntity<AthleteProgramStatsView> getAthleteProgramStats(UUID programId) {
        AthleteProgramStatsView stats = repository.getStatsByProgramId(programId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    public ResponseEntity<List<AthleteProgramStatsView>> getAthleteProgramStatsByWeek(UUID programId) {
        List<AthleteProgramStatsView> stats = repository.getWeeklyStatsByProgramId(programId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
