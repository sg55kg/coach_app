package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.enums.EffortIntensity;
import com.coachapp.coach_pc.enums.ExerciseType;
import com.coachapp.coach_pc.enums.WeightIntensity;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.exercise.ComplexExercise;
import com.coachapp.coach_pc.model.exercise.DurationExercise;
import com.coachapp.coach_pc.model.exercise.Exercise;
import com.coachapp.coach_pc.request.AthleteExerciseCommentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExerciseViewModel {
    public UUID id;
    public String name;
    public int weight;
    public int sets;
    public int repsPerSet;
    public String notes;
    public WeightIntensity weightIntensity = WeightIntensity.NONE;
    public boolean isMax;
    public int weightCompleted;
    public int totalRepsCompleted;
    public List<AthleteExerciseComment> comments = new ArrayList<>();
    public boolean isComplete;
    public int setsCompleted;
    public int order;
    public EffortIntensity effortIntensity = EffortIntensity.EASY;
    public boolean unilateral;
    public String equipment;
    public int distanceMeters;
    public int distanceCompletedMeters;
    public int secondsPerSet;
    public int secondsPerSetCompleted;
    public ExerciseType type = ExerciseType.EXERCISE;
    public List<Exercise> dropSets = new ArrayList<>();
    public List<Integer> repArr = new ArrayList<>();
    public List<String> nameArr = new ArrayList<>();
    public List<Integer> repCompletedArr = new ArrayList<>();
    public EffortIntensity actualIntensity = EffortIntensity.EASY;

    public ExerciseViewModel() {

    }

    public static ExerciseViewModel convertExercise(Exercise exercise) {
        ExerciseViewModel viewModel = new ExerciseViewModel();

        viewModel.comments = exercise.getComments();
        viewModel.id = exercise.getId();
        viewModel.dropSets = exercise.getDropSets();
        viewModel.isMax = exercise.getIsMax();
        viewModel.notes = exercise.getNotes();
        viewModel.isComplete = exercise.getIsComplete();
        viewModel.order = exercise.getOrder();
        viewModel.sets = exercise.getSets();
        viewModel.setsCompleted = exercise.getSetsCompleted();
        viewModel.type = exercise.getType();
        viewModel.weightCompleted = exercise.getWeightCompleted();
        viewModel.weight = exercise.getWeight();

        if (exercise.getType() == ExerciseType.EXERCISE) {
            viewModel.repsPerSet = exercise.getRepsPerSet();
            viewModel.totalRepsCompleted = exercise.getTotalRepsCompleted();
            viewModel.name = exercise.getName();
        } else if (exercise.getType() == ExerciseType.COMPLEX) {
            viewModel.nameArr = ComplexExercise.convertNameArrToList(
                    ((ComplexExercise) exercise).getNameArr()
            );
            viewModel.repArr = ComplexExercise.convertRepArrToList(
                    ((ComplexExercise) exercise).getRepArr()
            );
            viewModel.repCompletedArr = ComplexExercise.convertRepCompletedArrToList(
                    ((ComplexExercise) exercise).getRepCompletedArr()
            );
        } else if (exercise.getType() == ExerciseType.DURATION) {
            viewModel.effortIntensity = ((DurationExercise) exercise).getEffortIntensity();
            viewModel.actualIntensity = ((DurationExercise) exercise).getActualIntensity();
            viewModel.equipment = ((DurationExercise) exercise).getEquipment();
            viewModel.distanceMeters = ((DurationExercise) exercise).getDistanceMeters();
            viewModel.distanceCompletedMeters = ((DurationExercise) exercise).getDistanceCompletedMeters();
            viewModel.secondsPerSet = ((DurationExercise) exercise).getSecondsPerSet();
            viewModel.secondsPerSetCompleted = ((DurationExercise) exercise).getSecondsPerSetCompleted();
            viewModel.name = ((DurationExercise) exercise).getName();
        }

        return viewModel;
    }

}
