package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.request.UpdateProgramRequest;
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

    public ResponseEntity<Program> addProgram(ProgramRequest program, UUID coachId) {
        CoachData coach = new CoachData();
        AthleteData athlete = new AthleteData();
        athlete.setId(program.getAthleteId());
        coach.setId(coachId);

        Program newProgram = ProgramRequest.convertRequest(program);
        newProgram.setCoach(coach);
        newProgram.setAthlete(athlete);


        _programRepo.save(newProgram);
        return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
    }

    public ResponseEntity<ProgramViewModel> updateProgram(UpdateProgramRequest request, UUID id) {
        Optional<Program> dbProgram = _programRepo.findById(id);
        if(dbProgram.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Program program = UpdateProgramRequest.convertRequest(request, dbProgram.get());
        program = _programRepo.save(program);

        ProgramViewModel vm = convertProgram(program);
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
            programs.add(displayProgram);
        });

        return new ResponseEntity<>(programs, HttpStatus.OK);
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
            programs.add(displayProgram);
        });

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }
}
