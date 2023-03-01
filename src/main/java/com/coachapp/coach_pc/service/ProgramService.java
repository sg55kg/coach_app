package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.request.ExerciseRequest;
import com.coachapp.coach_pc.request.UpdateProgramRequest;
import com.coachapp.coach_pc.view.DisplayProgram;
import com.coachapp.coach_pc.view.ProgramViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.coachapp.coach_pc.repository.ProgramRepo;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class ProgramService {

    private ProgramRepo _programRepo;

    @Autowired
    public ProgramService(ProgramRepo programRepo) {
        this._programRepo = programRepo;
    }


    private void format(Day day, Program program) {
        day.setProgram(program);
        if (!day.getExercises().isEmpty()) {
            day.getExercises().forEach(e -> e.setDay(day));
        }
    }

    public List<DisplayProgram> getPrograms() {
        List<Program> programs = _programRepo.findAll();
        List<DisplayProgram> result = new ArrayList<>();


        programs.forEach(program -> {
            DisplayProgram displayProgram = new DisplayProgram(
                    program.getId(),
                    program.getCreatedAt(),
                    program.getUpdatedAt(),
                    program.getStartDate(),
                    program.getEndDate(),
                    program.getName());
            if (program.getAthlete() != null) {
                displayProgram.setAthleteId(program.getAthlete().getId());
            }
            OffsetDateTime d = getLastUpdatedDay(program);
            displayProgram.setLastEnteredDay(d);
            result.add(displayProgram);
        });

        return result;
    }

    public ProgramViewModel getProgram(UUID id) {
        Optional<Program> op = _programRepo.findById(id);
        if(op.isEmpty()) {
            return null;
        }

        Program raw = op.get();
        ProgramViewModel program = ProgramViewModel.convertProgram(raw);

        return program;
    }



    public void deleteProgram(UUID id) {
        _programRepo.deleteById(id);
    }

    public Program addProgram(Program program) {
        program = _programRepo.save(program);
        return program;
    }

    public ResponseEntity<ProgramViewModel> updateProgram(UpdateProgramRequest request, UUID id) {
        Optional<Program> dbProgram = _programRepo.findById(id);
        if(dbProgram.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Program program = UpdateProgramRequest.convertRequest(request, dbProgram.get());
        program = _programRepo.save(program);

        ProgramViewModel vm = ProgramViewModel.convertProgram(program);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    public ResponseEntity<List<DisplayProgram>> getProgramsByCoachId(UUID coachId) {
        List<Program> dbPrograms = _programRepo.getProgramsByCoachId(coachId);
        List<DisplayProgram> programs = new ArrayList<>();

        dbPrograms.forEach(program -> {
            DisplayProgram displayProgram = new DisplayProgram(
                    program.getId(),
                    program.getCreatedAt(),
                    program.getUpdatedAt(),
                    program.getStartDate(),
                    program.getEndDate(),
                    program.getName());
            if (program.getAthlete() != null) {
                displayProgram.setAthleteId(program.getAthlete().getId());
            }
            OffsetDateTime d = getLastUpdatedDay(program);
            displayProgram.setLastEnteredDay(d);
            programs.add(displayProgram);
        });

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    private OffsetDateTime getLastUpdatedDay(Program program) {
        OffsetDateTime result = OffsetDateTime.now();
        for (int i = 0; i <= program.getDays().size() - 1; i++) {
            Day day = program.getDays().get(i);

            if (!day.getIsRestDay() && day.getExercises().size() < 1) {
                result = day.getDate();
                break;
            }
        }
        return result;
    }

    public ResponseEntity<List<DisplayProgram>> getProgramsByTeamId(UUID teamId) {
        List<Program> dbPrograms = _programRepo.getProgramsByTeamId(teamId);
        List<DisplayProgram> programs = new ArrayList<>();

        dbPrograms.forEach(program -> {
            DisplayProgram displayProgram = new DisplayProgram(
                    program.getId(),
                    program.getCreatedAt(),
                    program.getUpdatedAt(),
                    program.getStartDate(),
                    program.getEndDate(),
                    program.getName());
            if (program.getAthlete() != null) {
                displayProgram.setAthleteId(program.getAthlete().getId());
            }
            OffsetDateTime d = getLastUpdatedDay(program);
            displayProgram.setLastEnteredDay(d);
            programs.add(displayProgram);
        });

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    public ResponseEntity<ProgramViewModel> updateProgramDay(UUID id, ExerciseRequest request) {
        Optional<Program> optional = _programRepo.findById(id);

        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Exercise exercise = ExerciseRequest.convertRequest(new Exercise(), request);
        Program program = optional.get();

        boolean dayFound = false;
//        for (Day day : program.getDays()) {
//            if (dayFound) break;
//
//            for (int i = 0; i < day.getExercises().size(); i++) {
//                if (day.getExercises().get(i).getId().equals(exercise.getId())) {
//                    exercise.setDay(day.getExercises().get(i).getDay());
//                    day.getExercises().set(i, exercise);
//                    dayFound = true;
//                    break;
//                }
//            }
//        }

        program = _programRepo.save(program);
        ProgramViewModel viewModel = ProgramViewModel.convertProgram(program);
        return new ResponseEntity<>(viewModel, HttpStatus.OK);
    }
}
