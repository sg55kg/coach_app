package com.coachapp.coach_pc.view.payment;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.coachapp.coach_pc.enums.PaymentStatus;
import com.coachapp.coach_pc.model.payment.AthletePaymentRecord;

@EntityView(AthletePaymentRecord.class)
@UpdatableEntityView
public interface UpdatableAthletePaymentRecordViewModel extends AthletePaymentRecordViewModel {

    void setAmountPaid(long amountPaid);
    void setStripeConnectId(String stripeConnectId);
    void setPaymentStatus(PaymentStatus paymentStatus);
}
