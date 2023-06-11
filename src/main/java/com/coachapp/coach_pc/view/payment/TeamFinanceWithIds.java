package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.payment.TeamFinance;

import java.util.UUID;

@EntityView(TeamFinance.class)
public interface TeamFinanceWithIds extends TeamFinanceViewModel {

    @Mapping("coach.id")
    UUID getCoachId();
    @Mapping("team.id")
    UUID getTeamId();
}
