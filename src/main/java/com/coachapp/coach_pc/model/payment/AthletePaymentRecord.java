package com.coachapp.coach_pc.model.payment;

import com.coachapp.coach_pc.enums.PaymentStatus;
import com.coachapp.coach_pc.model.user.AthleteData;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class AthletePaymentRecord {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @ManyToOne
    private TeamFinance teamFinance;
    @ManyToOne
    private AthleteData athlete;
    private String stripeConnectId;
    private long amountPaid;
    private PaymentStatus paymentStatus;
    private String stripeSessionId;

    public AthletePaymentRecord() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TeamFinance getTeamFinance() {
        return teamFinance;
    }

    public void setTeamFinance(TeamFinance teamFinance) {
        this.teamFinance = teamFinance;
    }

    public AthleteData getAthlete() {
        return athlete;
    }

    public void setAthlete(AthleteData athlete) {
        this.athlete = athlete;
    }

    public long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStripeConnectId() {
        return stripeConnectId;
    }

    public void setStripeConnectId(String stripeConnectId) {
        this.stripeConnectId = stripeConnectId;
    }

    public String getStripeSessionId() {
        return stripeSessionId;
    }

    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }
}
