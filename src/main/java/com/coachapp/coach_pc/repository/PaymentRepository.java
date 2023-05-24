package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.enums.PaymentStatus;
import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.payment.AthletePaymentRecord;
import com.coachapp.coach_pc.model.payment.TeamFinance;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.AthletePaymentRecordWithIds;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
import com.coachapp.coach_pc.view.payment.UpdatableTeamFinanceViewModel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
public class PaymentRepository {
    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public PaymentRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Transactional
    public TeamFinanceWithIds createTeamFinance(NewStripeAccountRequest request, String stripeConnectId) {
        CoachData coach = new CoachData();
        Team team = new Team();
        TeamFinance finance = new TeamFinance();
        coach.setId(request.getCoachId());
        team.setId(request.getTeamId());
        team.setTeamFinance(finance);
        finance.setCoach(coach);
        finance.setTeam(team);
        finance.setStripeStatus(StripeStatus.NEW);
        finance.setStripeConnectId(stripeConnectId);

        em.persist(finance);
        em.flush();

        TeamFinanceWithIds vm = evm.createBuilder(TeamFinanceWithIds.class)
                .with("id", finance.getId())
                .with("coachId", finance.getCoach().getId())
                .with("teamId", finance.getTeam().getId())
                .with("stripeStatus", finance.getStripeStatus())
                .with("stripeConnectId", finance.getStripeConnectId())
                .with("teamPrice", finance.getTeamPrice())
                .build();

        return vm;
    }

    @Transactional
    public void updateTeamFinanceStripeStatus(StripeStatus status, String stripeConnectId) {
        CriteriaBuilder<TeamFinance> cb = cbf.create(em, TeamFinance.class);
        UpdatableTeamFinanceViewModel teamFinance = evm
                .applySetting(EntityViewSetting.create(UpdatableTeamFinanceViewModel.class), cb)
                .where("stripeConnectId").eq(stripeConnectId)
                .getSingleResult();

        teamFinance.setStripeStatus(status);
        evm.save(em, teamFinance);
    }

    // TODO: Create Stripe payment request class
    @Transactional
    public AthletePaymentRecordWithIds createAthletePaymentRecord() {
        AthletePaymentRecord record = new AthletePaymentRecord();
        record.setAthlete(new AthleteData()); // TODO
        record.setPaymentStatus(PaymentStatus.NEW);
        record.setAmountPaid(1L); // TODO
        record.setTeamFinance(new TeamFinance()); // TODO
        em.persist(record);
        em.flush();

        CriteriaBuilder<AthletePaymentRecord> cb = cbf.create(em, AthletePaymentRecord.class);
        AthletePaymentRecordWithIds paymentRecord = evm
                .applySetting(EntityViewSetting.create(AthletePaymentRecordWithIds.class), cb)
                .where("stripeConnectId").eq("TODO")
                .getSingleResult();

        return paymentRecord;
    }

}
