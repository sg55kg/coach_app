package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.payment.TeamFinance;

@EntityView(TeamFinance.class)
@UpdatableEntityView
public interface UpdatableTeamFinanceViewModel extends TeamFinanceViewModel {

    void setStripeStatus(StripeStatus stripeStatus);
    void setTeamPrice(int teamPrice);
}
