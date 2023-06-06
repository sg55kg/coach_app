package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;

import java.util.List;
import java.util.UUID;

@EntityView(Team.class)
public interface DisplayTeam extends TeamViewModel {
    @Mapping(value = "COUNT(athletes)", fetch = FetchStrategy.SELECT)
    Long getNumAthletes();
    //List<AthleteViewModel> getAthletes();
    @Mapping("coach.user.username")
    String getCoachName();
    @Mapping("coach.id")
    UUID getCoachId();
    TeamFinanceViewModel getTeamFinance();
}
