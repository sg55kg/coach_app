package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.request.ProgramRequest;
import com.coachapp.coach_pc.request.UpdateProgramRequest;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProgramRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;
    private Logger logger = LoggerFactory.getLogger(ProgramRepository.class);

    public ProgramRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Transactional
    public Optional<ProgramWithDays> findById(UUID id) {
        CriteriaBuilder<Program> cb = cbf.create(em, Program.class);
        CriteriaBuilder<ProgramWithDays> cb1 = evm.applySetting(EntityViewSetting.create(ProgramWithDays.class), cb)
                .where("id").eq(id);

        try {
            return Optional.of(cb1.getSingleResult());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Optional.ofNullable(null);
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        Program program = em.find(Program.class, id);
        if (program != null) {
            em.remove(program);
        }
    }

    @Transactional
    public ProgramWithDays save(ProgramRequest program) {
//        InsertCriteriaBuilder<Program> cb = cbf.insert(em, Program.class);
//
//        if (program.getAthleteId() != null) {
//            cb.from(AthleteData.class, "a")
//                    .bind("athlete").select("a")
//                    .where("athlete.id").eq(program.getAthleteId());
//        }
//
//        if (program.getCoachId() != null) {
//            cb.from(CoachData.class, "c")
//                    .bind("coach").select("c")
//                    .where("coach.id").eq(program.getCoachId()).distinct();
//        }
//
//        if (program.getTeamId() != null) {
//            cb.from(Team.class, "t")
//                    .bind("team").select("t")
//                    .where("team.id").eq(program.getTeamId()).distinct();
//        }

        Program p = ProgramRequest.convertRequest(program);
        if (program.getTeamId() != null) {
            Team t = new Team();
            t.setId(program.getTeamId());
            p.setTeam(t);
        }

        if (program.getAthleteId() != null) {
            AthleteData a = em.find(AthleteData.class, program.getAthleteId());
            p.setAthlete(a);
            if (program.getIsCurrent()) {
                a.setCurrentProgram(p);
            }
            a.addProgram(p);
        }

        if (program.getCoachId() != null) {
            CoachData c = em.find(CoachData.class, program.getCoachId());
            c.addProgram(p);
            p.setCoach(c);
        }

        em.persist(p);


        ProgramWithDays result = evm.find(em, ProgramWithDays.class, p.getId());
        return result;
    }

    @Transactional
    public ProgramWithDays update(UpdateProgramRequest program, UUID id) {
        // This needs to be fixed, should just be able to pass in a program and merge it?
        Program p = em.find(Program.class, program.getId());
        p = UpdateProgramRequest.convertRequest(program, p);
        em.merge(p);
        ProgramWithDays result = evm.find(em, ProgramWithDays.class, program.getId());
        return result;
    }

    @Transactional
    public List<ProgramWithIds> getProgramsByCoachId(UUID coachId) {
        CriteriaBuilder<Program> cb = cbf.create(em, Program.class);
        CriteriaBuilder<ProgramWithIds> cb1 = evm.applySetting(EntityViewSetting.create(ProgramWithIds.class), cb)
                .where("coach.id").eq(coachId);

        List<ProgramWithIds> result = cb1.getResultList();
        return result;
    }

    @Transactional
    public List<ProgramWithIds> getProgramsByTeamId(UUID teamId) {
        CriteriaBuilder<Program> cb = cbf.create(em, Program.class);
        CriteriaBuilder<ProgramWithIds> cb1 = evm.applySetting(EntityViewSetting.create(ProgramWithIds.class), cb)
                .where("team.id").eq(teamId);

        List<ProgramWithIds> result = cb1.getResultList();
        return result;
    }

    @Transactional
    public List<ProgramWithIds> searchProgramsByName(UUID coachId, String name) {
        CriteriaBuilder<Program> cb = cbf.create(em, Program.class);
        CriteriaBuilder<ProgramWithIds> cb1 = evm.applySetting(EntityViewSetting.create(ProgramWithIds.class), cb)
                .where("name").like(false).value("%" + name + "%").noEscape()
                .where("coach.id").eq(coachId);

        List<ProgramWithIds> result = cb1.getResultList();
        return result;
    }
}
