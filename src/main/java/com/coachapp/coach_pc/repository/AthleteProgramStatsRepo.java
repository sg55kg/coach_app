package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.model.AthleteProgramStats;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.model.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AthleteProgramStatsRepo {

    @PersistenceContext
    EntityManager entityManager;


    public AthleteProgramStats getStats(UUID programId) throws Exception {
        entityManager.getTransaction().begin();

        Program program = entityManager.find(Program.class, programId);

        if (program == null) {
            throw new EntityNotFoundException("Could not find program with id: " + programId);
        }

        List<Day> completedDays = program.getDays().stream().filter(d ->
            d.getExercises()
                    .stream()
                    .filter(Exercise::getIsComplete)
                    .toList()
                    .size() == d.getExercises().size()
        ).toList();

        if (completedDays.size() < 1) {
            throw new Exception("Not enough days have been completed to generate stats for this program");
        }

        // Oly shorthand for Olympic Weightlifting, since we only want ratios of snatch/clean/jerk
        int totalOlyExercises = 0;
        int snatchExercises = 0;
        int cleanExercises = 0;
        int jerkExercises = 0;

        for (Day day : completedDays) {
            for (Exercise exercise : day.getExercises()) {
                if (exercise.getType() != ExerciseType.EXERCISE) {
                    continue;
                }
                if (exercise.getName().toLowerCase().contains("snatch")) {
                    snatchExercises++;
                    totalOlyExercises++;
                } else if (exercise.getName().toLowerCase().contains("clean") &&
                        exercise.getName().toLowerCase().contains("jerk")
                ) {
                    cleanExercises++;
                    jerkExercises++;
                    totalOlyExercises++;
                } else if (exercise.getName().toLowerCase().contains("clean")) {
                    cleanExercises++;
                    totalOlyExercises++;
                } else if (exercise.getName().toLowerCase().contains("jerk")) {
                    jerkExercises++;
                    totalOlyExercises++;
                }
            }
        }

        int percentSnatch = (int)((snatchExercises * 100.0f) / totalOlyExercises);
        int percentClean = (int)((cleanExercises * 100.0f) / totalOlyExercises);
        int percentJerk = (int)((jerkExercises * 100.0f) / totalOlyExercises);

        AthleteProgramStats stats = new AthleteProgramStats();

        stats.setPercentClean(percentClean);
        stats.setPercentJerk(percentJerk);
        stats.setPercentSnatch(percentSnatch);

        entityManager.persist(stats);
        entityManager.getTransaction().commit();

        return stats;
    }
}
