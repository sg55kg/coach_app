package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.payment.AthletePaymentRecord;

import java.util.UUID;

@EntityView(AthletePaymentRecord.class)
public interface AthletePaymentRecordWithIds extends AthletePaymentRecordViewModel {

    @Mapping("athlete.id")
    UUID getAthleteId();
    @Mapping("teamFinance.id")
    UUID getTeamFinanceId();
    @Mapping("athlete.user.email")
    String getAthleteUserEmail();

}
