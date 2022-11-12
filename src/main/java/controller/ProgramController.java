package controller;

import model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ProgramService;

import java.util.List;

@RestController("/api/programs")
@CrossOrigin("*")
public class ProgramController {

    private ProgramService _programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this._programService = programService;
    }

    @GetMapping
    public List<Program> getPrograms() {
        return _programService.getPrograms();
    }

    @PostMapping
    public Program addProgram(@RequestBody Program program) {
        return _programService.addProgram(program);
    }
}
