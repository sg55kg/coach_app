import {ExerciseType} from "../../../classes/program/exercise/enums";
import type {Program} from "../../../classes/program";

export const generateCSV = (program: Program) => {
    let str =
        'Day 1\nExercise,Weight,Sets,Reps,Complete,Weight Completed,RPS Completed,Sets Completed,Total Reps Completed,Notes\n';
    for (let i = 0; i < program.days.length; i++) {
        if (program.days[i].exercises.length < 1) continue;
        if (i > 0) {
            str +=
                'Day ' +
                (i + 1) +
                ': ' +
                program.days[i].date.format('ddd MMM DD') +
                '\n';
        }
        const d = program.days[i];
        if (d.isRestDay) {
            str += 'Rest Day\n';
            continue;
        }
        for (let j = 0; j < d.exercises.length; j++) {
            const e = d.exercises[j];
            if (e.type === ExerciseType.EXERCISE) {
                str +=
                    e.name +
                    ',' +
                    e.weight +
                    ',' +
                    e.sets +
                    ',' +
                    e.repsPerSet +
                    ',' +
                    e.isComplete +
                    ',' +
                    (e.isComplete ? e.weightCompleted : '0') +
                    ',' +
                    (e.isComplete && e.setsCompleted > 0
                        ? e.totalRepsCompleted / e.setsCompleted
                        : '0') +
                    ',' +
                    (e.isComplete ? e.setsCompleted : '0') +
                    ',' +
                    (e.isComplete ? e.totalRepsCompleted : '0') +
                    ',' +
                    e.notes +
                    '\n';
            } else if (e.type === ExerciseType.COMPLEX) {
                str +=
                    e.nameArr.join('+') +
                    ',' +
                    e.weight +
                    ',' +
                    e.sets +
                    ',' +
                    e.repArr.join('+') +
                    ',' +
                    e.isComplete +
                    ',' +
                    (e.isComplete ? e.weightCompleted : '0') +
                    ',' +
                    (e.isComplete
                        ? e.repCompletedArr.join('+')
                        : e.repArr.map(_ => 0).join('+')) +
                    ',' +
                    (e.isComplete ? e.setsCompleted : '0') +
                    ',' +
                    (e.isComplete
                        ? e.repArr.reduce((a, b) => a + b)
                        : '0') +
                    ',' +
                    e.notes +
                    '\n';
            }
        }
    }

    const downloadLink = document.createElement('a');
    const csv = str;
    const blob = new Blob(['\ufeff', csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const exportFilename = program.name;
    downloadLink.href = url;
    downloadLink.setAttribute('download', exportFilename);
    downloadLink.click();
};