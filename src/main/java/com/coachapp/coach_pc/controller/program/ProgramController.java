package com.coachapp.coach_pc.controller.program;


import com.coachapp.coach_pc.request.program.ProgramRequest;
import com.coachapp.coach_pc.request.program.UpdateProgramRequest;
// Unused imports (The Java compiler has to process each import statement, even if the imported class is not used.)
import com.coachapp.coach_pc.view.program.ProgramViewModel;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Unused imports (The Java compiler has to process each import statement, even if the imported class is not used.)
import org.springframework.security.core.parameters.P;
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;
import com.coachapp.coach_pc.service.program.ProgramService;


import java.util.List;
import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)
@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    // make instance dependency variables final to avoid accidental overwriting
    // underscores in instance variables are an antiquated leftover from the days when IDE's could not highlight instance vars for you
    private ProgramService _programService;
    private Logger logger = LoggerFactory.getLogger(ProgramController.class);

    @Autowired
    public ProgramController(ProgramService programService) {
        this._programService = programService;
    }

    @PostMapping("/coach/{coachId}")
    public ResponseEntity<ProgramWithDays> addProgram(@RequestBody ProgramRequest program, @PathVariable UUID coachId) {
        logger.info("Received request to add new program");
        return _programService.addProgram(program);
    }

    @GetMapping("/{id}")
    public ProgramWithDays getProgram(@PathVariable UUID id) {
        logger.info("Received Request to get program " + id);
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
    public ResponseEntity<ProgramWithDays> updateProgram(@PathVariable UUID id, @RequestBody UpdateProgramRequest program) {
        logger.info("Received request to update program " + id);
        return _programService.updateProgram(program, id);
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<ProgramWithIds>> getCoachPrograms(@PathVariable UUID coachId) {
        return _programService.getProgramsByCoachId(coachId);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<List<ProgramWithIds>> getTeamPrograms(@PathVariable UUID id) {
        logger.info("Received request to get programs by team id: " + id);
        return _programService.getProgramsByTeamId(id);
    }

    @GetMapping("/coach/{coachId}/search")
    public ResponseEntity<List<ProgramWithIds>> searchProgramsByName(@PathVariable UUID coachId, @RequestParam String name) {
        logger.info("Searching for programs similar to: " + name);
        return _programService.searchProgramsByName(coachId, name);
    }

    @GetMapping("/coach/{coachId}/templates")
    public ResponseEntity<List<ProgramWithIds>> getCoachTemplates(@PathVariable UUID coachId) {
        logger.info("Searching for program templates");
        return _programService.getCoachTemplates(coachId);
    }

// In general you never want commented out code in your main/master branch
// If you want a wip with work you intend to use later, keep it in a seperate branch
//    @PutMapping("/{id}/day")
//    public ResponseEntity<ProgramViewModel> updateProgramDay(@PathVariable UUID id, @RequestBody ExerciseRequest request) {
//        return _programService.updateProgramDay(id, request);
//    }
}
