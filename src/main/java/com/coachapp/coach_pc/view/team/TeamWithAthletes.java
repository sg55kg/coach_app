package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;

import java.util.List;

@EntityView(Team.class)
public interface TeamWithAthletes extends DisplayTeam {
    List<AthleteViewModel> getAthletes();
    @Mapping("teamFinance")
    TeamFinanceViewModel getTeamFinance();
}
