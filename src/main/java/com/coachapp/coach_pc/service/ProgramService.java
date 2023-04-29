package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.repository.ProgramRepository;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.request.UpdateProgramRequest;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProgramService {

    private ProgramRepository programRepo;

    @Autowired
    public ProgramService(ProgramRepository programRepo) {
        this.programRepo = programRepo;
    }


    private void format(Day day, Program program) {
        day.setProgram(program);
        if (!day.getExercises().isEmpty()) {
            day.getExercises().forEach(e -> e.setDay(day));
        }
    }

    public ProgramWithDays getProgram(UUID id) {
        Optional<ProgramWithDays> op = programRepo.findById(id);
        if(op.isEmpty()) {
            return null;
        }

        ProgramWithDays program = op.get();

        return program;
    }



    public void deleteProgram(UUID id) {
        programRepo.deleteById(id);
    }

    public ResponseEntity<ProgramWithDays> addProgram(ProgramRequest program) {
        ProgramWithDays result = programRepo.save(program);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    public ResponseEntity<ProgramWithDays> updateProgram(UpdateProgramRequest request, UUID id) {
        ProgramWithDays result = programRepo.update(request, id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<List<ProgramWithIds>> getProgramsByCoachId(UUID coachId) {
        List<ProgramWithIds> dbPrograms = programRepo.getProgramsByCoachId(coachId);

        return new ResponseEntity<>(dbPrograms, HttpStatus.OK);
    }

    public ResponseEntity<List<ProgramWithIds>> getProgramsByTeamId(UUID teamId) {
        List<ProgramWithIds> dbPrograms = programRepo.getProgramsByTeamId(teamId);

        return new ResponseEntity<>(dbPrograms, HttpStatus.OK);
    }

    public ResponseEntity<List<ProgramWithIds>> searchProgramsByName(UUID coachId, String name) {
        List<ProgramWithIds> dbPrograms = programRepo.searchProgramsByName(coachId, name);

        return new ResponseEntity<>(dbPrograms, HttpStatus.OK);
    }

//    public ResponseEntity<ProgramViewModel> updateProgramDay(UUID id, ExerciseRequest request) {
//        Optional<Program> optional = _programRepo.findById(id);
//
//        if (optional.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        Exercise exercise = ExerciseRequest.convertRequest(new Exercise(), request);
//        Program program = optional.get();
//
//        boolean dayFound = false;
////        for (Day day : program.getDays()) {
////            if (dayFound) break;
////
////            for (int i = 0; i < day.getExercises().size(); i++) {
////                if (day.getExercises().get(i).getId().equals(exercise.getId())) {
////                    exercise.setDay(day.getExercises().get(i).getDay());
////                    day.getExercises().set(i, exercise);
////                    dayFound = true;
////                    break;
////                }
////            }
////        }
//
//        program = _programRepo.save(program);
//        ProgramViewModel viewModel = ProgramViewModel.convertProgram(program);
//        return new ResponseEntity<>(viewModel, HttpStatus.OK);
//    }
}
