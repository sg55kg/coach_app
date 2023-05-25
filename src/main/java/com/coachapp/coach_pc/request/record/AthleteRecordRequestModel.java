package com.coachapp.coach_pc.request.record;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.AthleteRecord;
import com.coachapp.coach_pc.model.program.Day;
import com.coachapp.coach_pc.model.exercise.Exercise;

import java.util.UUID;

public class AthleteRecordRequestModel {
    private String exerciseName;
    private UUID exerciseId;
    private UUID dayId;
    private int numReps;
    private int weight;
    private boolean isCurrent;

    public AthleteRecordRequestModel() {}

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public UUID getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(UUID exerciseId) {
        this.exerciseId = exerciseId;
    }

    public UUID getDayId() {
        return dayId;
    }

    public void setDayId(UUID dayId) {
        this.dayId = dayId;
    }

    public static AthleteRecord convertRequest(AthleteRecordRequestModel request, UUID athleteId) {
        AthleteData athlete = new AthleteData();
        athlete.setId(athleteId);
        AthleteRecord record = new AthleteRecord();

        if (request.getDayId() != null) {
            Day day = new Day();
            day.setId(request.getDayId());
            record.setDay(day);
        }
        if (request.getExerciseId() != null) {
            Exercise exercise = new Exercise();
            exercise.setId(request.getExerciseId());
            exercise.setName(request.getExerciseName());
            record.setExercise(exercise);
        }

        record.setAthlete(athlete);
        record.setNumReps(request.getNumReps());
        record.setIsCurrent(true);
        record.setWeight(request.getWeight());
        record.setExerciseName(request.getExerciseName());

        return record;
    }
}
