package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.repository.AthleteRepo;
import com.coachapp.coach_pc.repository.AthleteRepository;
import com.coachapp.coach_pc.request.AthleteRequest;
import com.coachapp.coach_pc.request.record.AthleteRecordRequestModel;
import com.coachapp.coach_pc.view.AthleteViewModel;
import com.coachapp.coach_pc.view.record.AthleteRecordViewModel;
import com.coachapp.coach_pc.view.record.UpdatableAthleteRecordViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteService {

    private AthleteRepo athleteRepo;
    private AthleteRepository repository;

    public AthleteService(AthleteRepo athleteRepo, AthleteRepository repository) {
        this.athleteRepo = athleteRepo;
        this.repository = repository;
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

//    public ResponseEntity<List<AthleteRecord>> updateAthleteRecord(UUID athleteId, AthleteRecord record) {
//        Optional<AthleteData> optional = athleteRepo.findById(athleteId);
//
//        if (optional.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        AthleteData athlete = optional.get();
//        // Need logic if record has an id already that matches an existing record, it should update the existing record
//        boolean foundMatch = false;
////        for (int i = athlete.getRecords().size() - 1; i >= 0; i--) {
////            OffsetDateTime existingDate = athlete.getRecords().get(i).getLastUpdated();
////            if (existingDate.getDayOfMonth() == record.getLastUpdated().getDayOfMonth() &&
////                    existingDate.getYear() == record.getLastUpdated().getYear() &&
////                    existingDate.getMonth() == record.getLastUpdated().getMonth()
////            ) {
////                athlete.getRecords().get(i).copyValues(record);
////                foundMatch = true;
////                break;
////            }
////        }
////        if (!foundMatch) {
////            athlete.addRecord(record);
////            record.setAthlete(athlete);
////        }
//
//        athlete = athleteRepo.save(athlete);
//
//        return new ResponseEntity<>(athlete.getRecords(), HttpStatus.OK);
//    }

    public AthleteData getAthleteData(UUID athleteId) {
        Optional<AthleteData> optional = athleteRepo.findById(athleteId);

        return optional.get();
    }

    public ResponseEntity<List<AthleteRecordViewModel>> getAthleteRecordsByExerciseName(
            UUID athleteId,
            String name,
            Integer reps,
            Integer weight,
            Boolean current
    ) {
        if (name == null) {
            // TODO: query for 5-10 common exercises, likely for an overview or stats page
            // TODO: if weight/reps aren't null factor those into the search
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else if (reps == null && weight == null) {
            // TODO: query for records between 1-10 reps and any weight with an exercise name
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else if (reps == null) {
            // TODO: query for any record at or above a certain weight
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else if (weight == null) {
            // TODO: query for records between 1-10 reps
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            // TODO: query for the last PR for an exercise
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AthleteRecordViewModel> getAthleteRecordByExerciseId(UUID athleteId, UUID exerciseId) {
        return null;
    }

    public ResponseEntity<List<AthleteRecordViewModel>> getAthleteRecordsByDayId(UUID athleteId, UUID dayId) {
        return null;
    }

    public ResponseEntity<AthleteRecordViewModel> createAthleteRecord(UUID athleteId, AthleteRecordRequestModel request) {
        UpdatableAthleteRecordViewModel lastRecord = repository.getLastRecord(athleteId, request);

        if (lastRecord == null) {
            AthleteRecord newRecord = AthleteRecordRequestModel.convertRequest(request, athleteId);
            var result = repository.addRecord(newRecord, null);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else if (request.getWeight() > lastRecord.getWeight()) {
            lastRecord.setIsCurrent(false);
            AthleteRecord newRecord = AthleteRecordRequestModel.convertRequest(request, athleteId);
            var result = repository.addRecord(newRecord, lastRecord);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    public ResponseEntity<List<AthleteRecordViewModel>> createAthleteRecords(
            UUID athleteId, List<AthleteRecordRequestModel> request
    ) {
        List<String> exerciseNames = request.stream().map(AthleteRecordRequestModel::getExerciseName).toList();
        List<UpdatableAthleteRecordViewModel> prevRecords = repository.getLastRecordBatch(athleteId, exerciseNames);
        List<AthleteRecord> newRecords = new ArrayList<>();
        List<UpdatableAthleteRecordViewModel> expiredRecords = new ArrayList<>();

        // Match any previous records to new records
        for (AthleteRecordRequestModel r : request) {
            Optional<UpdatableAthleteRecordViewModel> optional = prevRecords.stream().filter(p -> {
                if (p.getExerciseName().equalsIgnoreCase(r.getExerciseName()) &&
                    p.getNumReps() == r.getNumReps()) {
                    return true;
                }
                return false;
            }).findFirst();

            if (optional.isEmpty()) {
                newRecords.add(AthleteRecordRequestModel.convertRequest(r, athleteId));
            } else if (optional.get().getWeight() < r.getWeight()) {
                var oldRecord = optional.get();
                oldRecord.setIsCurrent(false);
                expiredRecords.add(oldRecord);
                newRecords.add(AthleteRecordRequestModel.convertRequest(r, athleteId));
            }
        }

        // Update records that are no longer current and persist new records
        if (newRecords.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_MODIFIED);
        } else {
            List<AthleteRecordViewModel> vms = repository.addRecords(newRecords, prevRecords);
            return new ResponseEntity<>(vms, HttpStatus.CREATED);
        }
    }
}
