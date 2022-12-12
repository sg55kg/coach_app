package com.coachapp.coach_pc.controller;

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

    @Autowired
    public ProgramController(ProgramService programService) {
        this._programService = programService;
    }

    @GetMapping
    public List<DisplayProgram> getPrograms() {
        return _programService.getPrograms();
    }

    @PostMapping("/coach/{coachId}")
    public ResponseEntity<Program> addProgram(@RequestBody ProgramRequest program, @PathVariable UUID coachId) {
        return _programService.addProgram(program, coachId);
    }

    @GetMapping("/{id}")
    public ProgramViewModel getProgram(@PathVariable UUID id) {
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
        return _programService.updateProgram(program, id);
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<DisplayProgram>> getCoachPrograms(@PathVariable UUID coachId) {
        return _programService.getProgramsByCoachId(coachId);
    }
}
