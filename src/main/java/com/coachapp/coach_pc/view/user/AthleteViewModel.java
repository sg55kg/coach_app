package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteRecord;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.view.DisplayTeam;
import com.coachapp.coach_pc.view.program.ProgramViewModel;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;
import com.coachapp.coach_pc.view.team.TeamViewModel;

import java.util.List;
import java.util.UUID;

@EntityView(AthleteData.class)
public interface AthleteViewModel {

    @IdMapping
    UUID getId();
    @Mapping("name")
    String getName();
    @Mapping(value = "currentProgram")
    ProgramWithDays getCurrentProgram();
//    @Limit(limit = "10", order = "lastUpdated DESC")
//    List<AthleteRecord> getRecords();
    @Mapping("team")
    TeamViewModel getTeam();
}
