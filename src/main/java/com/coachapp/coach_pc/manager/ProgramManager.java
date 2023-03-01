package com.coachapp.coach_pc.manager;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.repository.AthleteRepo;
import com.coachapp.coach_pc.repository.CoachRepo;
import com.coachapp.coach_pc.repository.TeamRepo;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.service.AthleteService;
import com.coachapp.coach_pc.service.CoachService;
import com.coachapp.coach_pc.service.ProgramService;
import com.coachapp.coach_pc.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProgramManager {

    private final ProgramService programService;
    private final AthleteRepo athleteRepo;
    private final CoachRepo coachRepo;
    private final TeamRepo teamRepo;

    @Autowired
    public ProgramManager(
            ProgramService programService,
            AthleteRepo athleteRepo,
            CoachRepo coachRepo,
            TeamRepo teamRepo
    ) {
        this.programService = programService;
        this.athleteRepo = athleteRepo;
        this.coachRepo = coachRepo;
        this.teamRepo = teamRepo;
    }

    public ResponseEntity<Program> createProgram(ProgramRequest programRequest) {
        Program program = ProgramRequest.convertRequest(programRequest);

        program = programService.addProgram(program);

        if (programRequest.getAthleteId() != null) {
            Optional<AthleteData> optional = athleteRepo.findById(programRequest.getAthleteId());
            AthleteData athlete = optional.get();
            if (programRequest.getIsCurrent()) {
                athlete.setCurrentProgram(program);
                athleteRepo.save(athlete);
            }
            program.setAthlete(athlete);

        }
        if (programRequest.getCoachId() != null) {
            Optional<CoachData> optional = coachRepo.findById(programRequest.getCoachId());
            CoachData coach = optional.get();
            program.setCoach(coach);

            program = programService.addProgram(program);
        }
        if (programRequest.getTeamId() != null) {
            Optional<Team> optional = teamRepo.findById(programRequest.getTeamId());
            Team team = optional.get();
            program.setTeam(team);

            program = programService.addProgram(program);
        }
        return new ResponseEntity<>(program, HttpStatus.CREATED);
    }
}
