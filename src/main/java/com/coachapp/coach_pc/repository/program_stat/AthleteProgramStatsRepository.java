package com.coachapp.coach_pc.repository.program_stat;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.program.Program;
import com.coachapp.coach_pc.model.exercise.ComplexExercise;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.view.programStats.AthleteProgramStatsView;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.lang.Integer.parseInt;

// Add logs to every repo method indicating request params (masking private data) and response values for debugging
@Repository
public class AthleteProgramStatsRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public AthleteProgramStatsRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
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
                } else if (exercise.getType() == ExerciseType.COMPLEX) {
                    var splitExercises = splitComplexExercise((ComplexExercise) exercise);
                    mainLifts.addAll(splitExercises);
                }
            }
        }

        for (Exercise e : mainLifts) {
            totalRepsPlanned += e.getRepsPerSet() * e.getSets();
            totalRepsActual += e.getTotalRepsCompleted();
            totalVolumePlanned += (((double)e.getRepsPerSet() * e.getSets()) * e.getWeight());
            totalVolumeActual += (double)e.getTotalRepsCompleted() * e.getWeightCompleted();
            for (Exercise d : e.getDropSets()) {
                totalRepsPlanned += formatDouble(d.getRepsPerSet() * e.getSets());
                totalRepsActual += formatDouble(d.getTotalRepsCompleted());
                totalVolumePlanned += formatDouble(((double)d.getRepsPerSet() * d.getSets()) * d.getWeight());
                totalVolumeActual += formatDouble((double)d.getTotalRepsCompleted() * d.getWeightCompleted());
            }
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
        stats.setStartDate(program.getStartDate());
        stats.setEndDate(program.getEndDate());

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

        List<Day> days = program.getDays().stream().sorted(Comparator.comparing(Day::getDate)).toList();
        for (Day day : days) {
            if (dayCount == 7) {
                dayCount = 0;
                result.add(currentStats);
                startDate = day.getDate();
                currentStats = new AthleteProgramStatsView();
                totalRepsPlanned = 0;
                totalRepsActual = 0;
                totalVolumePlanned = 0.0;
                totalVolumeActual = 0.0;
                averageIntensityPlanned = 0.0;
                averageIntensityActual = 0.0;
                kValuePlanned = 0.0;
                kValueActual = 0.0;
                inolPlanned = 0.0;
                inolActual = 0.0;
            }
            List<Exercise> exercises = new ArrayList<>();
            for (Exercise e : day.getExercises()) {
                if (e.getType() == ExerciseType.EXERCISE) {
                    exercises.add(e);
                } else if (e.getType() == ExerciseType.COMPLEX) {
                    var splitExercises = splitComplexExercise((ComplexExercise) e);
                    exercises.addAll(splitExercises);
                }
            }

            endDate = day.getDate();

            for (Exercise e : exercises) {
                totalRepsPlanned += formatDouble(e.getRepsPerSet() * e.getSets());
                totalRepsActual += formatDouble(e.getTotalRepsCompleted());
                totalVolumePlanned += formatDouble(((double)e.getRepsPerSet() * e.getSets()) * e.getWeight());
                totalVolumeActual += formatDouble((double)e.getTotalRepsCompleted() * e.getWeightCompleted());
                for (Exercise d : e.getDropSets()) {
                    totalRepsPlanned += formatDouble(d.getRepsPerSet() * e.getSets());
                    totalRepsActual += formatDouble(d.getTotalRepsCompleted());
                    totalVolumePlanned += formatDouble(((double)d.getRepsPerSet() * d.getSets()) * d.getWeight());
                    totalVolumeActual += formatDouble((double)d.getTotalRepsCompleted() * d.getWeightCompleted());
                }
            }

            averageIntensityPlanned = totalVolumePlanned > 0 ? formatDouble(totalVolumePlanned / totalRepsPlanned) : 0.0;
            averageIntensityActual = totalVolumeActual > 0 ? formatDouble(totalVolumeActual / totalRepsActual) : 0.0;

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

    public List<AthleteProgramStatsView> getDailyBreakdowns(UUID programId, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<Day> days = cbf.create(em, Day.class)
                .where("program.id").eq(programId)
                .where("date").between(startDate).and(endDate)
                .getResultList();

        List<AthleteProgramStatsView> result = new ArrayList<>();

        for (Day day : days) {
            AthleteProgramStatsView currentStats = new AthleteProgramStatsView();
            OffsetDateTime currentDate = day.getDate();
            List<Exercise> exercises = new ArrayList<>();
            for (Exercise e : day.getExercises()) {
                if (e.getType() == ExerciseType.EXERCISE) {
                    exercises.add(e);
                } else if (e.getType() == ExerciseType.COMPLEX) {
                    var splitExercises = splitComplexExercise((ComplexExercise) e);
                    exercises.addAll(splitExercises);
                }
            }

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

            for (Exercise e : exercises) {
                totalRepsPlanned += formatDouble(e.getRepsPerSet() * e.getSets());
                totalRepsActual += formatDouble(e.getTotalRepsCompleted());
                totalVolumePlanned += formatDouble(((double)e.getRepsPerSet() * e.getSets()) * e.getWeight());
                totalVolumeActual += formatDouble((double)e.getTotalRepsCompleted() * e.getWeightCompleted());
                for (Exercise d : e.getDropSets()) {
                    totalRepsPlanned += formatDouble(d.getRepsPerSet() * e.getSets());
                    totalRepsActual += formatDouble(d.getTotalRepsCompleted());
                    totalVolumePlanned += formatDouble(((double)d.getRepsPerSet() * d.getSets()) * d.getWeight());
                    totalVolumeActual += formatDouble((double)d.getTotalRepsCompleted() * d.getWeightCompleted());
                }
            }

            averageIntensityPlanned = totalVolumePlanned > 0 ? formatDouble(totalVolumePlanned / totalRepsPlanned) : 0.0;
            averageIntensityActual = totalVolumeActual > 0 ? formatDouble(totalVolumeActual / totalRepsActual) : 0.0;

            // TODO: potentially rename this property so stats can be used on a program or day level
            currentStats.setProgramId(day.getId());

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
            currentStats.setStartDate(currentDate);
            currentStats.setEndDate(currentDate);

            result.add(currentStats);
        }

        return result;
    }

    private List<Exercise> splitComplexExercise(ComplexExercise complex) {
        List<Exercise> result = new ArrayList<>();
        var names = complex.getNameArr().split(",");
        var reps = complex.getRepArr().split(",");
        var repsCompleted = complex.getRepCompletedArr().split(",");

        for (int i = 0; i < names.length; i++) {
            Exercise ex = new Exercise();
            ex.setName(names[i]);
            ex.setRepsPerSet(parseInt(reps[i]));
            ex.setSets(complex.getSets());
            ex.setWeight(complex.getWeight());
            ex.setIsComplete(complex.getIsComplete());
            if (repsCompleted.length >= i+1) {
                // TODO: This is a sign data needs to be cleaned before saving in the first place
                if (repsCompleted[i] != null && !Objects.equals(repsCompleted[i], "null") && !Objects.equals(repsCompleted[i], "")) {
                    ex.setTotalRepsCompleted(parseInt(repsCompleted[i]) * complex.getSetsCompleted());
                }
            }
            ex.setSetsCompleted(complex.getSetsCompleted());

            result.add(ex);
        }
        return result;
    }

    private double formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("#.#####");
        String formatted = df.format(d);

        return Double.parseDouble(formatted);
    }
}
