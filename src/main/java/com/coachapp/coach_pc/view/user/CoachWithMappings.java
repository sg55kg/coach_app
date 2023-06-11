package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.view.team.CoachListTeam;
import com.coachapp.coach_pc.view.team.TeamViewModel;

import java.util.List;

@EntityView(CoachData.class)
public interface CoachWithMappings extends CoachViewModel {

    List<AthleteViewModel> getAthletes();

    List<CoachListTeam> getTeams();
}
