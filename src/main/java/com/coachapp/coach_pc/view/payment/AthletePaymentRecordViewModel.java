package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.coachapp.coach_pc.enums.PaymentStatus;
import com.coachapp.coach_pc.model.payment.AthletePaymentRecord;

import java.time.OffsetDateTime;
import java.util.UUID;


@EntityView(AthletePaymentRecord.class)
public interface AthletePaymentRecordViewModel {

    @IdMapping
    UUID getId();
    OffsetDateTime getCreatedAt();
    long getAmountPaid();
    String getStripeConnectId();
    String getStripeSessionId();
    PaymentStatus getPaymentStatus();
}
