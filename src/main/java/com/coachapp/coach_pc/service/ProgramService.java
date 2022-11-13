package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coachapp.coach_pc.repository.ProgramRepo;

import java.util.List;
import java.util.UUID;

@Service
public class ProgramService {

    private ProgramRepo _programRepo;

    @Autowired
    public ProgramService(ProgramRepo programRepo) {
        this._programRepo = programRepo;
    }

    public Program addProgram(ProgramRequest programRequest) {
        Program program = new Program();

        program.setDays(programRequest.getDays());
        program.getDays().forEach(e -> e.setProgram(program));
        program.setName(programRequest.getName());

        return _programRepo.save(program);
    }

    public List<Program> getPrograms() {
        return _programRepo.findAll();
    }

    public Program getProgram(UUID id) {
        return _programRepo.findById(id).orElse(null);
    }
}
