package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.coachapp.coach_pc.service.ProgramService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
@CrossOrigin("*")
public class ProgramController {

    private ProgramService _programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this._programService = programService;
    }

    @GetMapping("/")
    public List<Program> getPrograms() {
        return _programService.getPrograms();
    }

    @PostMapping
    public Program addProgram(@RequestBody ProgramRequest program) {
        return _programService.addProgram(program);
    }

    @GetMapping("/{id}")
    public Program getProgram(@PathVariable UUID id) {
        return _programService.getProgram(id);
    }
}
