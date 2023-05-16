package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.payment.TeamFinance;

import java.util.UUID;

@EntityView(TeamFinance.class)
public interface TeamFinanceViewModel {
    @IdMapping
    UUID getId();
    @Mapping("coach.id")
    UUID getCoachId();
    @Mapping("team.id")
    UUID getTeamId();
    String getStripeConnectId();
    StripeStatus getStripeStatus();
    int getTeamPrice();
}
