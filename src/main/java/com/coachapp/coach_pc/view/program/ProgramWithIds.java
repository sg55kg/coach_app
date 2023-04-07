package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.Program;

import java.util.UUID;

@EntityView(Program.class)
public interface ProgramWithIds extends ProgramViewModel {

    @Mapping("athlete.id")
    UUID getAthleteId();
    @Mapping("athlete.name")
    String getAthleteName();
    @Mapping("coach.id")
    UUID getCoachId();
    @Mapping("team.id")
    UUID getTeamId();
}
