package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.request.TokenRequest;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.view.DayViewModel;
import com.coachapp.coach_pc.view.DisplayProgram;
import com.coachapp.coach_pc.view.ProgramViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.coachapp.coach_pc.repository.ProgramRepo;

import java.util.*;

@Service
public class ProgramService {

    private ProgramRepo _programRepo;

    @Autowired
    public ProgramService(ProgramRepo programRepo) {
        this._programRepo = programRepo;
    }

    public Program addProgram(ProgramRequest programRequest) {
        Program program = new Program();
        List<Day> days = new ArrayList<>();

        programRequest.getDays().forEach(d -> days.add(new Day(d.getDate(), d.getExercises())));
        program.setDays(days);
        program.getDays().forEach(d -> format(d, program));
        program.setName(programRequest.getName());
        program.setStartDate(programRequest.getStartDate());
        program.setEndDate(programRequest.getEndDate());

        return _programRepo.save(program);
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
        ProgramViewModel program = convertProgram(raw);

        return program;
    }

    private ProgramViewModel convertProgram(Program program) {
        ProgramViewModel viewModel = new ProgramViewModel();

        viewModel.setId(program.getId());
        List<DayViewModel> set = new ArrayList<>();
        program.getDays().forEach(d ->
                set.add(new DayViewModel(
                        d.getId(),
                        d.getDate(),
                        d.getExercises()
                ))
        );
        viewModel.setDays(set);
        viewModel.setStartDate(program.getStartDate());
        viewModel.setEndDate(program.getEndDate());
        viewModel.setName(program.getName());

        return viewModel;
    }

    public void deleteProgram(UUID id) {
        _programRepo.deleteById(id);
    }

    public Program updateProgram(ProgramRequest program, UUID id) {
        boolean exists = _programRepo.existsById(id);
        if(exists) {
            Program detachedProgram = new Program();
            List<Day> days = new ArrayList<>();
         
            detachedProgram.setEndDate(program.getEndDate());
            detachedProgram.setName(program.getName());
            detachedProgram.setStartDate(program.getStartDate());
            detachedProgram.setId(program.getId());
            detachedProgram.setDays(days);
            program.getDays().forEach(d ->
                    days.add(Day.convertRequest(d, detachedProgram))
            );
            return _programRepo.save(detachedProgram);
        }
        return null;
    }

    public ResponseEntity<List<DisplayProgram>> getProgramsByCoachId(UUID coachId, TokenRequest tokenRequest) {
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
            programs.add(displayProgram);
        });

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }
}
