package com.coachapp.coach_pc.repository.user;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.WhereOrBuilder;
import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.Program;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.AthleteRecord;
import com.coachapp.coach_pc.request.record.AthleteRecordRequestModel;
import com.coachapp.coach_pc.view.program.DayViewModel;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.record.AthleteRecordViewModel;
import com.coachapp.coach_pc.view.record.AthleteRecordWithIds;
import com.coachapp.coach_pc.view.record.UpdatableAthleteRecordViewModel;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;
import com.coachapp.coach_pc.view.user.AthleteWithPrograms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AthleteRepository {
    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;
    private final Logger logger = LoggerFactory.getLogger(AthleteRepository.class);

    public AthleteRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Transactional
    public Optional<AthleteWithPrograms> findById(UUID athleteId) {
        AthleteWithPrograms athlete = evm.find(em, AthleteWithPrograms.class, athleteId);
        return Optional.ofNullable(athlete);
    }

    @Transactional
    public boolean existsById(UUID athleteId) {
        Long count = cbf.create(em, Long.class)
                .from(AthleteData.class, "a")
                .select("COUNT(id)")
                .getSingleResult();
        return count > 0;
    }

    /**
     * Temporary way to update athlete until there is a better way to associate a program to an athlete via entity view
     *
     * @deprecated
     */
    @Deprecated
    @Transactional
    public AthleteData save(AthleteData athlete) {
        em.merge(athlete);
        em.flush();
        return athlete;
    }

    @Transactional
    public AthleteRecordViewModel addRecord(AthleteRecord record, UpdatableAthleteRecordViewModel lastRecord) {
        try {
            if (lastRecord != null) {
                // Service handles setting isCurrent to false, save the changes
                evm.save(em, lastRecord);
            }
            em.persist(record);
            em.flush();

            EntityViewBuilder<AthleteRecordWithIds> builder = createAthleteRecordViewModelBuilder(record);
            AthleteRecordViewModel vm = builder.build();

            return vm;
        } catch (Exception ex) {
            logger.error("Error saving new record\n");
            logger.error(record.toString());
            logger.error(lastRecord.toString());
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public List<AthleteRecordViewModel> addRecords(
            List<AthleteRecord> newRecords, List<UpdatableAthleteRecordViewModel> prevRecords
    ) {
        for (var prev : prevRecords) {
            evm.save(em, prev);
        }
        List<AthleteRecordViewModel> result = new ArrayList<>();
        for (var record : newRecords) {
            em.persist(record);
        }
        em.flush();
        for (var record : newRecords) {
            EntityViewBuilder<AthleteRecordWithIds> builder = createAthleteRecordViewModelBuilder(record);
            AthleteRecordViewModel vm = builder.build();
            result.add(vm);
        }

        return result;
    }

    @Transactional
    public UpdatableAthleteRecordViewModel getLastRecord(UUID athleteId, AthleteRecordRequestModel request) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        try {
            UpdatableAthleteRecordViewModel vm = evm.applySetting(EntityViewSetting.create(UpdatableAthleteRecordViewModel.class), cb)
                    .where("athlete.id").eq(athleteId)
                    .where("isCurrent").eq(true)
                    .where("exerciseName").like(false).value(request.getExerciseName()).noEscape()
                    .where("numReps").eq(request.getNumReps())
                    .getSingleResult();

            return vm;
        } catch (NoResultException ex) {
            return null;
        } catch (IncorrectResultSizeDataAccessException ex) {
            logger.error("Error retrieving last record for athlete: " + athleteId);
            logger.error(request.toString());
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public List<UpdatableAthleteRecordViewModel> getLastRecordBatch(
            UUID athleteId,
            List<String> names
    ) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        try {
            var query =
                    evm.applySetting(EntityViewSetting.create(UpdatableAthleteRecordViewModel.class), cb)
                    .where("athlete.id")
                        .eq(athleteId)
                    .where("isCurrent")
                        .eq(true)
                    .whereOr();

            for (String name : names) {
                query = query.where("exerciseName").like(false).value(name).noEscape();
            }

            List<UpdatableAthleteRecordViewModel> results = query.endOr().getResultList();
            return results;
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            logger.error("Error fetching last record batch for athlete: " + athleteId);
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public List<AthleteRecordViewModel> getAthleteRecordsByExerciseNames(UUID athleteId, List<String> exerciseNames) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        try {
            var query =
                    evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                            .where("athlete.id")
                            .eq(athleteId)
                            .where("isCurrent")
                            .eq(true)
                            .whereOr();

            for (String name : exerciseNames) {
                query = query.where("exerciseName").like(false).value(name).noEscape();
            }

            List<AthleteRecordViewModel> results = query.endOr().getResultList();
            return results;
        } catch (NoResultException ex) {
            return new ArrayList<>();
        } catch (Exception ex) {
            logger.error("Error fetching last record batch for athlete: " + athleteId);
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public Optional<AthleteData> findEntityById(UUID id) {
        return Optional.ofNullable(em.find(AthleteData.class, id));
    }

    public AthleteViewModel convertEntityToViewModel(AthleteData athlete) {
        return createAthleteViewModelBuilder(athlete).build();
    }

    private EntityViewBuilder<AthleteRecordWithIds> createAthleteRecordViewModelBuilder(AthleteRecord record) {
        EntityViewBuilder<AthleteRecordWithIds> builder = evm.createBuilder(AthleteRecordWithIds.class)
                .with("athleteId", record.getAthlete().getId())
                .with("isCurrent", record.getIsCurrent())
                .with("id", record.getId())
                .with("numReps", record.getNumReps())
                .with("exerciseName", record.getExerciseName())
                .with("weight", record.getWeight())
                .with("createdAt", record.getCreatedAt());
        if (record.getExercise() != null) {
            builder = builder.with("exerciseId", record.getExercise().getId());
        }
        if (record.getDay() != null) {
            builder = builder.with("dayId", record.getDay().getId());
        }
        return builder;
    }

    private EntityViewBuilder<AthleteViewModel> createAthleteViewModelBuilder(AthleteData athlete) {
        EntityViewBuilder<AthleteViewModel> builder = evm.createBuilder(AthleteViewModel.class)
                .with("id", athlete.getId())
                .with("name", athlete.getName());

        if (athlete.getCurrentProgram() != null) {
            builder.with("currentProgram", createProgramViewModelBuilder(athlete.getCurrentProgram()).build());
        } else {
            builder.with("currentProgram", athlete.getCurrentProgram());
        }

        if (athlete.getTeam() != null) {
            builder.with("team", createTeamViewModelBuilder(athlete.getTeam()).build());
        } else {
            builder.with("team", athlete.getTeam());
        }

        return builder;
    }

    private EntityViewBuilder<ProgramWithDays> createProgramViewModelBuilder(Program program) {
        EntityViewBuilder<ProgramWithDays> builder = evm.createBuilder(ProgramWithDays.class)
                .with("id", program.getId())
                .with("name", program.getName())
                .with("startDate", program.getStartDate())
                .with("endDate", program.getEndDate())
                .with("updatedAt", program.getUpdatedAt())
                .with(
                        "days",
                        program.getDays()
                                .stream()
                                .map(d -> createDayViewModelBuilder(d).build())
                                .toList()
                );
        return builder;
    }

    private EntityViewBuilder<DayViewModel> createDayViewModelBuilder(Day day) {
        EntityViewBuilder<DayViewModel> builder = evm.createBuilder(DayViewModel.class)
                .with("id", day.getId())
                .with("date", day.getDate())
                .with("isRestDay", day.getIsRestDay())
                .with("warmUp", day.getWarmUp())
                .with("exercises", day.getExercises());
        return builder;
    }

    private EntityViewBuilder<TeamViewModel> createTeamViewModelBuilder(Team team) {
        EntityViewBuilder<TeamViewModel> builder = evm.createBuilder(TeamViewModel.class)
                .with("id", team.getId())
                .with("name", team.getName())
                .with("description", team.getDescription())
                .with("createdAt", team.getCreatedAt())
                .with("teamLogo", team.getTeamLogo());
        return builder;
    }

    @Transactional
    public List<AthleteRecordViewModel> getCommonAthleteRecords(UUID athleteId, Boolean isCurrent) {
        try {
            List<String> commonExerciseNames =
                    new ArrayList<>(
                            List.of("Snatch", "Clean and jerk", "Back squat", "Front Squat", "Deadlift", "Bench Press")
                    );
            CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
            CriteriaBuilder<AthleteRecordViewModel> query =
                    evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                            .where("athlete.id")
                            .eq(athleteId)
                            .where("numReps")
                            .eq(1);

            if (isCurrent) {
                query.where("isCurrent").eq(isCurrent);
            }

            WhereOrBuilder<CriteriaBuilder<AthleteRecordViewModel>> or = query.whereOr();

            for (String exercise : commonExerciseNames) {
                or.where("exerciseName").like(false).value(exercise).noEscape();
            }

            return or.endOr().getResultList();
        } catch (Exception ex) {
            logger.error("Error retrieving common records");
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public List<AthleteRecordViewModel> getAthleteRecordsByName(UUID athleteId, String name, Boolean isCurrent) {
        try {
            CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
            var query = evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                    .where("athlete.id")
                    .eq(athleteId)
                    .where("exerciseName")
                    .like(false).value(name).noEscape();

            if (isCurrent) {
                query.where("isCurrent").eq(isCurrent);
            }

            return query.getResultList();
        } catch (Exception ex) {
            logger.error("Error retrieving records by name " + name + " current: " + isCurrent);
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public List<AthleteRecordViewModel> getAthleteRecordsByWeight(UUID athleteId, String name, Integer weight, Boolean isCurrent) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        var query = evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                .where("athlete.id")
                    .eq(athleteId)
                .where("exerciseName")
                    .like(false).value(name).noEscape()
                .where("weight")
                    .gt(weight);

        if (isCurrent) {
            query.where("isCurrent").eq(isCurrent);
        }

        return query.getResultList();
    }

    @Transactional
    public List<AthleteRecordViewModel> getAthleteRecordsByReps(UUID athleteId, String name, Integer reps, Boolean isCurrent) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        CriteriaBuilder<AthleteRecordViewModel> query = evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                .where("athlete.id").eq(athleteId)
                .where("exerciseName").like(false).value(name).noEscape()
                .where("numReps").eq(reps);

        if (isCurrent) {
            query.where("isCurrent").eq(isCurrent);
        }

        return query.getResultList();
    }
}
