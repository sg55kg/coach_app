package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.repository.AthleteRepo;
import com.coachapp.coach_pc.request.AthleteRequest;
import com.coachapp.coach_pc.view.AthleteViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
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

    public ResponseEntity<AthleteViewModel> updateAthlete(UUID id, AthleteRequest request) {
        Optional<AthleteData> optional = athleteRepo.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = optional.get();
        athlete = AthleteRequest.convertRequest(request, athlete);
        athlete = athleteRepo.save(athlete);
        AthleteViewModel vm = AthleteViewModel.convertAthlete(athlete);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    public ResponseEntity<List<AthleteRecord>> updateAthleteRecord(UUID athleteId, AthleteRecord record) {
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = optional.get();
        // Need logic if record has an id already that matches an existing record, it should update the existing record
        boolean foundMatch = false;
        for (int i = athlete.getRecords().size() - 1; i >= 0; i--) {
            OffsetDateTime existingDate = athlete.getRecords().get(i).getLastUpdated();
            if (existingDate.getDayOfMonth() == record.getLastUpdated().getDayOfMonth() &&
                    existingDate.getYear() == record.getLastUpdated().getYear() &&
                    existingDate.getMonth() == record.getLastUpdated().getMonth()
            ) {
                athlete.getRecords().get(i).copyValues(record);
                foundMatch = true;
                break;
            }
        }
        if (!foundMatch) {
            athlete.addRecord(record);
            record.setAthlete(athlete);
        }

        athlete = athleteRepo.save(athlete);

        return new ResponseEntity<>(athlete.getRecords(), HttpStatus.OK);
    }

    public AthleteData getAthleteData(UUID athleteId) {
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        return optional.get();
    }
}
