package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.repository.AthleteRepo;
import com.coachapp.coach_pc.request.AthleteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteService {

    private AthleteRepo athleteRepo;

    public AthleteService(AthleteRepo athleteRepo) {
        this.athleteRepo = athleteRepo;
    }

    public ResponseEntity<AthleteData> setCurrentProgram(UUID athleteId, UUID programId) {
        boolean exists = athleteRepo.existsById(athleteId);

        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athleteData = new AthleteData();
        Program program = new Program();

        program.setId(programId);
        athleteData.setId(athleteId);
        athleteData.setCurrentProgram(program);

        athleteData = athleteRepo.save(athleteData);

        return new ResponseEntity<>(athleteData, HttpStatus.OK);
    }

    public ResponseEntity<AthleteData> updateAthlete(UUID id, AthleteRequest request) {
        boolean exists = athleteRepo.existsById(id);

        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = AthleteRequest.convertRequest(request);
        athlete = athleteRepo.save(athlete);

        return new ResponseEntity<>(athlete, HttpStatus.OK);
    }

    public ResponseEntity<AthleteRecord> updateAthleteRecord(UUID athleteId, AthleteRecord record) {
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = optional.get();
        athlete.setRecords(record);
        record.setAthlete(athlete);
        athlete = athleteRepo.save(athlete);

        return new ResponseEntity<>(athlete.getRecords(), HttpStatus.OK);
    }
}
