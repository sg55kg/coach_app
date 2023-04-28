package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.team.TeamViewModel;

import java.util.UUID;

@EntityView(AthleteData.class)
public interface AthleteViewModel {

    @IdMapping
    UUID getId();
    @Mapping("name")
    String getName();
    @Mapping(value = "currentProgram", fetch = FetchStrategy.SELECT)
    ProgramWithDays getCurrentProgram();
    @Mapping("team")
    TeamViewModel getTeam();
}
