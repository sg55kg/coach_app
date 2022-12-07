package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.repository.CoachRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<CoachData> createCoachData(CoachData coachData) {
        CoachData coach = coachRepo.save(coachData);
        return new ResponseEntity<>(coach, HttpStatus.OK);
    }

    public ResponseEntity<CoachData> updateCoachData(CoachData data) {
        CoachData coach = coachRepo.save(data);
        return new ResponseEntity<>(coach, HttpStatus.OK);
    }
}
