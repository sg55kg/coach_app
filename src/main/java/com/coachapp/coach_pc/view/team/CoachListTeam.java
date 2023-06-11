package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;

@EntityView(Team.class)
public interface CoachListTeam extends TeamViewModel {

    @Mapping(value = "COUNT(athletes)", fetch = FetchStrategy.SELECT)
    Long getNumAthletes();
    @Mapping("teamFinance")
    TeamFinanceViewModel getTeamFinance();
}
