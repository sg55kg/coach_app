package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.view.program.ProgramViewModel;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;
import com.coachapp.coach_pc.view.team.TeamViewModel;

import java.util.List;

@EntityView(CoachData.class)
public interface CoachWithMappings extends CoachViewModel {

    List<AthleteViewModel> getAthletes();

    List<TeamViewModel> getTeams();
//    @Mapping("programsList")
//    List<ProgramWithIds> getPrograms();
}
