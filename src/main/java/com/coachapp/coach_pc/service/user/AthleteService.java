package com.coachapp.coach_pc.service.user;

import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.AthleteRecord;
import com.coachapp.coach_pc.model.program.Program;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.repository.user.AthleteRepository;
import com.coachapp.coach_pc.request.user.AthleteRequest;
import com.coachapp.coach_pc.request.record.AthleteRecordRequestModel;
import com.coachapp.coach_pc.view.record.AthleteRecordViewModel;
import com.coachapp.coach_pc.view.record.UpdatableAthleteRecordViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;
import com.coachapp.coach_pc.view.user.AthleteWithPrograms;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteService {

    private final AthleteRepository repository;

    public AthleteService(AthleteRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<AthleteData> setCurrentProgram(UUID athleteId, UUID programId) {
        boolean exists = repository.existsById(athleteId);

        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athleteData = new AthleteData();
        Program program = new Program();

        program.setId(programId);
        athleteData.setId(athleteId);
        athleteData.setCurrentProgram(program);

        athleteData = repository.save(athleteData);

        return new ResponseEntity<>(athleteData, HttpStatus.OK);
    }

    public ResponseEntity<AthleteViewModel> updateAthlete(UUID id, AthleteRequest request) {
        Optional<AthleteData> optional = repository.findEntityById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AthleteData athlete = optional.get();

        if (request.getCoachId() != null) {
            CoachData coach = new CoachData();
            Team team = new Team();
            team.setId(request.getTeamId());
            coach.setId(request.getCoachId());
            athlete.setCoach(coach);
            athlete.setTeam(team);
        } else {
            athlete.setCoach(null);
            athlete.setTeam(null);
        }

        repository.save(athlete);
        AthleteViewModel result = repository.convertEntityToViewModel(athlete);
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    public AthleteWithPrograms getAthleteData(UUID athleteId) {
        Optional<AthleteWithPrograms> optional = repository.findById(athleteId);

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
            List<AthleteRecordViewModel> records = repository.getCommonAthleteRecords(athleteId, current);
            // TODO: if weight/reps aren't null factor those into the search
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else if (reps == null && weight == null) {
            List<AthleteRecordViewModel> records = repository.getAthleteRecordsByName(athleteId, name, current);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else if (reps == null) {
            List<AthleteRecordViewModel> records = repository.getAthleteRecordsByWeight(athleteId, name, weight, current);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else if (weight == null) {
            // TODO: query for records between 1-10 reps
            List<AthleteRecordViewModel> records = repository.getAthleteRecordsByReps(athleteId, name, reps, current);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else if (current) {
            // TODO: query for the last PR for an exercise
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AthleteRecordViewModel> getAthleteRecordByExerciseId(UUID athleteId, UUID exerciseId) {
        // TODO
        return null;
    }

    public ResponseEntity<List<AthleteRecordViewModel>> getAthleteRecordsByDayId(UUID athleteId, UUID dayId) {
        // TODO
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
            return new ResponseEntity<>(null, HttpStatus.OK);
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
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        } else {
            List<AthleteRecordViewModel> vms = repository.addRecords(newRecords, prevRecords);
            return new ResponseEntity<>(vms, HttpStatus.CREATED);
        }
    }
}
