package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.manager.ProgramManager;
import com.coachapp.coach_pc.request.ExerciseRequest;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.request.UpdateProgramRequest;
import com.coachapp.coach_pc.view.DisplayProgram;
import com.coachapp.coach_pc.view.ProgramViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.coachapp.coach_pc.service.ProgramService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private ProgramService _programService;
    private ProgramManager programManager;

    @Autowired
    public ProgramController(ProgramService programService, ProgramManager programManager) {
        this._programService = programService;
        this.programManager = programManager;
    }

    @GetMapping
    public List<DisplayProgram> getPrograms() {
        return _programService.getPrograms();
    }

    @PostMapping("/coach/{coachId}")
    public ResponseEntity<Program> addProgram(@RequestBody ProgramRequest program, @PathVariable UUID coachId) {
        System.out.println("Received request to add new program");
        return programManager.createProgram(program);
    }

    @GetMapping("/{id}")
    public ProgramViewModel getProgram(@PathVariable UUID id) {
        System.out.println("Received Request to get program " + id);
        return _programService.getProgram(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProgram(@PathVariable UUID id) {
        try {
            _programService.deleteProgram(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramViewModel> updateProgram(@PathVariable UUID id, @RequestBody UpdateProgramRequest program) {
        System.out.println("Received request to update program " + id);
        return _programService.updateProgram(program, id);
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<DisplayProgram>> getCoachPrograms(@PathVariable UUID coachId) {
        return _programService.getProgramsByCoachId(coachId);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<List<DisplayProgram>> getTeamPrograms(@PathVariable UUID id) {
        System.out.println("Received request to get programs by team id: " + id);
        return _programService.getProgramsByTeamId(id);
    }

    @PutMapping("/{id}/day")
    public ResponseEntity<ProgramViewModel> updateProgramDay(@PathVariable UUID id, @RequestBody ExerciseRequest request) {
        return _programService.updateProgramDay(id, request);
    }
}
