package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.ExerciseType;
import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class ComplexExercise extends Exercise {


    // These are stored as comma separated values in the DB
    private String nameArr;
    private String repArr;
    private String repCompletedArr;
    @Transient
    private final ExerciseType type = ExerciseType.COMPLEX;

    public ComplexExercise() {}

    public String getNameArr() {
        return nameArr;
    }

    public void setNameArr(String nameArr) {
        this.nameArr = nameArr;
    }

    public String getRepArr() {
        return repArr;
    }

    public void setRepArr(String repArr) {
        this.repArr = repArr;
    }

    public String getRepCompletedArr() {
        return repCompletedArr;
    }

    public void setRepCompletedArr(String repCompletedArr) {
        this.repCompletedArr = repCompletedArr;
    }

    public static List<String> convertNameArrToList(String names) {
        List<String> nameArr = List.of(names.split(","));
        return nameArr;
    }

    public static List<Integer> convertRepArrToList(String reps) {
        List<String> repStrArr = List.of(reps.split(","));
        List<Integer> repArr = repStrArr
                .stream()
                .filter(Objects::nonNull)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return repArr;
    }

    public static List<Integer> convertRepCompletedArrToList(String repsCompleted) {
        if (repsCompleted.length() < 1) {
            return List.of(0);
        }
        List<String> repCompletedStrArr = List.of(repsCompleted.split(","));
        List<Integer> repCompletedArr = repCompletedStrArr
                .stream()
                .filter(n -> !n.equals("null"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return repCompletedArr;
    }

    public static String convertNameArrListToString(List<String> names) {
        String namesArr = names
                .stream()
                .collect(Collectors.joining(","));
        return namesArr;
    }

    public static String convertRepArrListToString(List<Integer> reps) {
        String repsArr = reps
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return repsArr;
    }

    public static String convertRepCompletedArrListToString(List<Integer> repsCompleted) {
        String repsCompletedArr = repsCompleted
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return repsCompletedArr;
    }

    @Override
    public ExerciseType getType() {
        return type;
    }
}
