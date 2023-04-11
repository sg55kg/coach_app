package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.view.programStats.AthleteProgramStatsView;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class AthleteProgramStatsRepository {

    private final EntityManager em;

    public AthleteProgramStatsRepository(EntityManager em) {
        this.em = em;
    }

    public AthleteProgramStatsView getStatsByProgramId(UUID programId) {
        Program program = em.find(Program.class, programId);
        List<Exercise> mainLifts = new ArrayList<>();
        int totalRepsPlanned = 0;
        int totalRepsActual = 0;
        double totalVolumePlanned = 0.0;
        double totalVolumeActual = 0.0;
        double averageIntensityPlanned = 0.0;
        double averageIntensityActual = 0.0;
        double kValuePlanned = 0.0;
        double kValueActual = 0.0;
        double inolPlanned = 0.0;
        double inolActual = 0.0;

        for (Day day : program.getDays()) {
            for (Exercise exercise : day.getExercises()) {
                if (exercise.getType() == ExerciseType.EXERCISE) {
                    mainLifts.add(exercise);
                }
            }
        }

        for (Exercise e : mainLifts) {
            totalRepsPlanned += e.getRepsPerSet() * e.getSets();
            totalRepsActual += e.getTotalRepsCompleted();
            totalVolumePlanned += (((double)e.getRepsPerSet() * e.getSets()) * e.getWeight());
            totalVolumeActual += (double)e.getTotalRepsCompleted() * e.getWeightCompleted();
        }

        averageIntensityPlanned = totalVolumePlanned / totalRepsPlanned;
        averageIntensityActual = totalVolumeActual > 0 ? totalVolumeActual / totalRepsActual : 0.0;

        AthleteProgramStatsView stats = new AthleteProgramStatsView();

        stats.setProgramId(programId);
        stats.setActualAverageIntensity(averageIntensityActual);
        stats.setPlannedAverageIntensity(averageIntensityPlanned);
        stats.setActualTotalReps(totalRepsActual);
        stats.setPlannedTotalReps(totalRepsPlanned);
        stats.setActualTotalVolume(totalVolumeActual);
        stats.setPlannedTotalVolume(totalVolumePlanned);
        stats.setActualKValue(kValueActual);
        stats.setPlannedKValue(kValuePlanned);
        stats.setActualInol(inolActual);
        stats.setPlannedInol(inolPlanned);
        stats.setProgramName(program.getName());
        stats.setStartDate(program.getStartDate().toInstant().atOffset(ZoneOffset.UTC));
        stats.setEndDate(program.getEndDate().toInstant().atOffset(ZoneOffset.UTC));

        return stats;
    }

    public List<AthleteProgramStatsView> getWeeklyStatsByProgramId(UUID programId) {
        Program program = em.find(Program.class, programId);
        List<AthleteProgramStatsView> result = new ArrayList<>();

        OffsetDateTime startDate = program.getStartDate().toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime endDate;
        int dayCount = 0;
        int totalCount = 0;
        AthleteProgramStatsView currentStats = new AthleteProgramStatsView();

        int totalRepsPlanned = 0;
        int totalRepsActual = 0;
        double totalVolumePlanned = 0.0;
        double totalVolumeActual = 0.0;
        double averageIntensityPlanned = 0.0;
        double averageIntensityActual = 0.0;
        double kValuePlanned = 0.0;
        double kValueActual = 0.0;
        double inolPlanned = 0.0;
        double inolActual = 0.0;

        for (Day day : program.getDays()) {
            if (dayCount == 7) {
                result.add(currentStats);
                startDate = day.getDate();
                currentStats = new AthleteProgramStatsView();
                totalRepsPlanned = 0;
                totalRepsActual = 0;
                averageIntensityPlanned = 0.0;
                averageIntensityActual = 0.0;
                kValuePlanned = 0.0;
                kValueActual = 0.0;
                inolPlanned = 0.0;
                inolActual = 0.0;
            }

            endDate = day.getDate();

            for (Exercise e : day.getExercises()) {
                if (e.getType() != ExerciseType.EXERCISE) {
                    continue;
                }
                totalRepsPlanned += e.getRepsPerSet() * e.getSets();
                totalRepsActual += e.getTotalRepsCompleted();
                totalVolumePlanned += (((double)e.getRepsPerSet() * e.getSets()) * e.getWeight());
                totalVolumeActual += (double)e.getTotalRepsCompleted() * e.getWeightCompleted();
            }

            averageIntensityPlanned = totalVolumePlanned > 0 ? totalVolumePlanned / totalRepsPlanned : 0.0;
            averageIntensityActual = totalVolumeActual > 0 ? totalVolumeActual / totalRepsActual : 0.0;

            currentStats.setProgramId(programId);
            currentStats.setActualAverageIntensity(averageIntensityActual);
            currentStats.setPlannedAverageIntensity(averageIntensityPlanned);
            currentStats.setActualTotalReps(totalRepsActual);
            currentStats.setPlannedTotalReps(totalRepsPlanned);
            currentStats.setActualTotalVolume(totalVolumeActual);
            currentStats.setPlannedTotalVolume(totalVolumePlanned);
            currentStats.setActualKValue(kValueActual);
            currentStats.setPlannedKValue(kValuePlanned);
            currentStats.setActualInol(inolActual);
            currentStats.setPlannedInol(inolPlanned);
            currentStats.setProgramName(program.getName());
            currentStats.setStartDate(startDate);
            currentStats.setEndDate(endDate);

            dayCount++;
            totalCount++;
            if (totalCount == program.getDays().size() - 1) {
                result.add(currentStats);
            }
        }
        return result;
    }
}
