package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.payment.TeamFinance;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
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
    public TeamFinanceViewModel createTeamFinance(NewStripeAccountRequest request, String stripeConnectId) {
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

        TeamFinanceViewModel vm = evm.createBuilder(TeamFinanceViewModel.class)
                .with("id", finance.getId())
                .with("coachId", finance.getCoach().getId())
                .with("teamId", finance.getTeam().getId())
                .with("stripeStatus", finance.getStripeStatus())
                .with("stripeConnectId", finance.getStripeConnectId())
                .with("teamPrice", finance.getTeamPrice())
                .build();

        return vm;
    }

}
