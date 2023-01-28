package com.coachapp.coach_pc.model.exercise;

import com.coachapp.coach_pc.enums.ExerciseType;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Table(name = "complex_exercise")
public class ComplexExercise extends Exercise {

    private List<String> nameArr = new ArrayList<>();
    private List<Integer> repArr = new ArrayList<>();
    private List<Integer> repCompletedArr = new ArrayList<>();
    @Transient
    private final ExerciseType type = ExerciseType.COMPLEX;

    public ComplexExercise() {}

    public List<String> getNameArr() {
        return nameArr;
    }

    public void setNameArr(List<String> nameArr) {
        this.nameArr = nameArr;
    }

    public List<Integer> getRepArr() {
        return repArr;
    }

    public void setRepArr(List<Integer> repArr) {
        this.repArr = repArr;
    }

    public List<Integer> getRepCompletedArr() {
        return repCompletedArr;
    }

    public void setRepCompletedArr(List<Integer> repCompletedArr) {
        this.repCompletedArr = repCompletedArr;
    }

}
