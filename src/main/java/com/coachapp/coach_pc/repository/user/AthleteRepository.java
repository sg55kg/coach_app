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
import com.coachapp.coach_pc.view.record.UpdatableAthleteRecordViewModel;
import com.coachapp.coach_pc.view.team.TeamViewModel;
import com.coachapp.coach_pc.view.user.AthleteViewModel;
import com.coachapp.coach_pc.view.user.AthleteWithPrograms;
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
        if (lastRecord != null) {
            // Service handles setting isCurrent to false, save the changes
            evm.save(em, lastRecord);
        }
        em.persist(record);
        em.flush();

        EntityViewBuilder<AthleteRecordViewModel> builder = createAthleteRecordViewModelBuilder(record);
        AthleteRecordViewModel vm = builder.build();

        return vm;
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
            EntityViewBuilder<AthleteRecordViewModel> builder = createAthleteRecordViewModelBuilder(record);
            AthleteRecordViewModel vm = builder.build();
            result.add(vm);
        }

        return result;
    }

    public UpdatableAthleteRecordViewModel getLastRecord(UUID athleteId, AthleteRecordRequestModel request) {
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        try {
            UpdatableAthleteRecordViewModel vm = evm.applySetting(EntityViewSetting.create(UpdatableAthleteRecordViewModel.class), cb)
                    .where("athlete.id").eq(athleteId)
                    .where("isCurrent").eq(true)
                    .where("exercise.name").like(false).value(request.getExerciseName()).noEscape()
                    .where("numReps").eq(request.getNumReps())
                    .getSingleResult();

            return vm;
        } catch (NoResultException ex) {
            return null;
        }
    }

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
                query = query.where("exercise.name").like(false).value(name).noEscape();
            }

            List<UpdatableAthleteRecordViewModel> results = query.endOr().getResultList();
            return results;
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Optional<AthleteData> findEntityById(UUID id) {
        return Optional.ofNullable(em.find(AthleteData.class, id));
    }

    public AthleteViewModel convertEntityToViewModel(AthleteData athlete) {
        return createAthleteViewModelBuilder(athlete).build();
    }

    private EntityViewBuilder<AthleteRecordViewModel> createAthleteRecordViewModelBuilder(AthleteRecord record) {
        EntityViewBuilder<AthleteRecordViewModel> builder = evm.createBuilder(AthleteRecordViewModel.class)
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

    public List<AthleteRecordViewModel> getCommonAthleteRecords(UUID athleteId) {
        List<String> commonExerciseNames = new ArrayList<>(List.of("Snatch", "Clean and jerk", "Back squat", "Front Squat", "Deadlift", "Bench Press"));
        CriteriaBuilder<AthleteRecord> cb = cbf.create(em, AthleteRecord.class);
        WhereOrBuilder<CriteriaBuilder<AthleteRecordViewModel>> query =
                evm.applySetting(EntityViewSetting.create(AthleteRecordViewModel.class), cb)
                .where("athlete.id").eq(athleteId)
                .where("isCurrent").eq(true)
                .whereOr();

        for (String exercise : commonExerciseNames) {
            query.where("exercise.name").like(false).value(exercise).noEscape();
        }
        return query.endOr().getResultList();
    }
}
