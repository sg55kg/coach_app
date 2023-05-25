package com.coachapp.coach_pc.repository.team;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.request.team.NewTeamRequest;
import com.coachapp.coach_pc.request.team.TeamRequest;
import com.coachapp.coach_pc.view.team.DisplayTeam;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.team.TeamWithAthletes;
import com.coachapp.coach_pc.view.team.UpdatableTeamViewModel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeamRepository {
    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public TeamRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Transactional
    public Optional<TeamWithAthletes> findById(UUID teamId) {
        try {
            return Optional.ofNullable(evm.find(em, EntityViewSetting.create(TeamWithAthletes.class), teamId));
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    // TODO: paginate and add search options when there are more than 5 teams on the main site
    @Transactional
    public List<DisplayTeam> getDisplayTeams() {
        CriteriaBuilder<Team> cb = cbf.create(em, Team.class);
        List<DisplayTeam> displayTeams = evm.applySetting(EntityViewSetting.create(DisplayTeam.class), cb)
                .getResultList();

        return displayTeams;
    }


    @Transactional
    public TeamViewModel updateTeam(TeamRequest request) {
        UpdatableTeamViewModel vm = evm.createBuilder(UpdatableTeamViewModel.class)
                .with("id", request.getId())
                .with("name", request.getName())
                .with("teamLogo", request.getTeamLogo())
                .with("description", request.getDescription())
                .build();

        evm.save(em, vm);
        return vm;
    }

    @Transactional
    public TeamViewModel createTeam(NewTeamRequest request) {
        Team team = new Team();
        CoachData coach = new CoachData();
        coach.setId(request.getCoachId());
        team.setCoach(coach);
        team.setDescription(request.getDescription());
        team.setTeamLogo(request.getTeamLogo());
        team.setName(request.getName());

        em.persist(team);
        em.flush();

        UpdatableTeamViewModel vm = evm.createBuilder(UpdatableTeamViewModel.class)
                .with("id", team.getId())
                .with("coachId", request.getCoachId())
                .with("teamLogo", team.getTeamLogo())
                .with("description", team.getDescription())
                .with("name", team.getName())
                .with("createdAt", team.getCreatedAt())
                .build();

        return vm;
    }

    @Transactional
    public List<TeamWithAthletes> findAllByCoachId(UUID coachId) {
        CriteriaBuilder<Team> cb = cbf.create(em, Team.class);
        List<TeamWithAthletes> teams = evm.applySetting(EntityViewSetting.create(TeamWithAthletes.class), cb)
                .where("coach.id").eq(coachId)
                .getResultList();

        return teams;
    }
}
