package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.repository.CoachRepo;
import com.coachapp.coach_pc.request.CoachRequest;
import com.coachapp.coach_pc.request.NewCoachRequest;
import com.coachapp.coach_pc.request.ProgramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachService {

    private final CoachRepo coachRepo;

    @Autowired
    public CoachService(CoachRepo coachRepo) {
        this.coachRepo = coachRepo;
    }

    public ResponseEntity<CoachData> getCoachData(UUID id) {
        Optional<CoachData> data = coachRepo.findById(id);
        if (data.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        }
    }

    public ResponseEntity<CoachData> updateCoachData(CoachRequest data) {
        CoachData coach = new CoachData();
        coach.setId(data.getId());
        List<Program> programs = new ArrayList<>();

        for (ProgramRequest r : data.getPrograms()) {
            Program program = ProgramRequest.convertRequest(r);
            program.setCoach(coach);
            programs.add(program);
        }

        coach.setPrograms(programs);

        CoachData updatedCoach = coachRepo.save(coach);
        return new ResponseEntity<>(updatedCoach, HttpStatus.OK);
    }

    public ResponseEntity<CoachData> addProgram(ProgramRequest programRequest, UUID id) {
        Optional<CoachData> c = coachRepo.findById(id);

        if (c.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        CoachData coach = c.get();
        AthleteData athlete = new AthleteData();
        athlete.setId(programRequest.getAthleteId());



        List<Program> programs = coach.getPrograms();

        Program program = ProgramRequest.convertRequest(programRequest);

        program.setCoach(coach);
        program.setAthlete(athlete);

        programs.add(program);
        coach = coachRepo.save(coach);

        return new ResponseEntity<>(coach, HttpStatus.CREATED);
    }
}
