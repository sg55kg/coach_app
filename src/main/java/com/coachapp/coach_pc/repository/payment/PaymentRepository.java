package com.coachapp.coach_pc.repository.payment;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.enums.PaymentStatus;
import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.payment.AthletePaymentRecord;
import com.coachapp.coach_pc.model.payment.TeamFinance;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.request.payment.AthletePaymentRequest;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.AthletePaymentRecordWithIds;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
import com.coachapp.coach_pc.view.payment.UpdatableAthletePaymentRecordViewModel;
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
        finance.setAthleteCap(request.getAthleteCap());
        finance.setInviteOnly(false);
        finance.setRequestRequired(false);

        em.persist(finance);
        em.flush();

        TeamFinanceWithIds vm = evm.createBuilder(TeamFinanceWithIds.class)
                .with("id", finance.getId())
                .with("coachId", finance.getCoach().getId())
                .with("teamId", finance.getTeam().getId())
                .with("stripeStatus", finance.getStripeStatus())
                .with("stripeConnectId", finance.getStripeConnectId())
                .with("teamPrice", finance.getTeamPrice())
                .with("athleteCap", finance.getAthleteCap())
                .with("inviteOnly", finance.getInviteOnly())
                .with("requestRequired", finance.getRequestRequired())
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

    @Transactional
    public AthletePaymentRecordWithIds createAthletePaymentRecord(AthletePaymentRequest request, String sessionId) {
        AthletePaymentRecord record = getAthletePaymentRecord(request);
        record.setStripeSessionId(sessionId);
        em.persist(record);
        em.flush();

        CriteriaBuilder<AthletePaymentRecord> cb = cbf.create(em, AthletePaymentRecord.class);
        AthletePaymentRecordWithIds paymentRecord = evm
                .applySetting(EntityViewSetting.create(AthletePaymentRecordWithIds.class), cb)
                .where("stripeSessionId").eq(sessionId)
                .getSingleResult();

        return paymentRecord;
    }

    @Transactional
    public UpdatableAthletePaymentRecordViewModel getUpdatableAthletePaymentRecord(String sessionId) {
        // TODO
        CriteriaBuilder<AthletePaymentRecord> cb = cbf.create(em, AthletePaymentRecord.class);
        UpdatableAthletePaymentRecordViewModel paymentRecord = evm
                .applySetting(EntityViewSetting.create(UpdatableAthletePaymentRecordViewModel.class), cb)
                .where("stripeSessionId").eq(sessionId)
                .getSingleResult();

        return paymentRecord;
    }

    private static AthletePaymentRecord getAthletePaymentRecord(AthletePaymentRequest request) {
        AthletePaymentRecord record = new AthletePaymentRecord();

        AthleteData athleteData = new AthleteData();
        athleteData.setId(request.getAthleteId());

        CoachData coachData = new CoachData();
        coachData.setId(request.getCoachId());

        TeamFinance teamFinance = new TeamFinance();
        teamFinance.setId(request.getTeamFinanceId());

        record.setAthlete(athleteData); // TODO - make sure if we need to connect all relationships
        record.setPaymentStatus(PaymentStatus.NEW);
        record.setAmountPaid(request.getPrice());
        record.setTeamFinance(teamFinance);

        return record;
    }

    private EntityViewBuilder<UpdatableAthletePaymentRecordViewModel> createUpdatablePaymentRecordBuilder(
            AthletePaymentRecordWithIds record) {
        EntityViewBuilder<UpdatableAthletePaymentRecordViewModel> builder =
                evm.createBuilder(UpdatableAthletePaymentRecordViewModel.class)
                        .with("id", record.getId())
                        .with("createdAt", record.getCreatedAt())
                        .with("paymentStatus", record.getPaymentStatus())
                        .with("stripeConnectId", record.getStripeConnectId())
                        .with("stripeSessionId", record.getStripeSessionId());

        return builder;
    }

}
