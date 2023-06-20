import type { Writable } from 'svelte/store';
import { Program } from '../classes/program';
import { writable } from 'svelte/store';
import type { Day } from '../classes/program/day';
import { ExerciseType } from '../classes/program/exercise/enums';
import { ProgramService } from '../service/ProgramService';
import type { Exercise } from '../classes/program/exercise';
import UserService from '../service/UserService';
import { AthleteRecord } from '../classes/user/athlete/records';
import {AthleteRecordError, ExerciseMaxRepsError, ExerciseMaxWeightError} from "../errors/athlete/athleteErrors";

let currentProgram: Writable<Program> = writable(new Program());
let currentDay: Writable<Day | undefined> = writable(undefined);
let currentDayIdx: Writable<number> = writable(-1);
let athleteProgramLoading: Writable<boolean> = writable(false);
let athleteProgramSuccess: Writable<string> = writable('');
let athleteProgramError: Writable<Error | undefined> = writable();
let athleteId: Writable<string> = writable('');
let newRecordExerciseIds: Writable<string[]> = writable([]);

const markDayCompleteAsWritten = async (
    athleteId: string,
    day: Day,
    program: Program
) => {
    athleteProgramLoading.set(true);
    athleteProgramSuccess.set('');
    athleteProgramError.set(undefined);
    try {
        const dayCopy = JSON.parse(JSON.stringify(day)) as Day;
        dayCopy.exercises.forEach(e => {
            e = markExerciseFieldsComplete(e);
            e.dropSets.forEach(d => {
                d = markExerciseFieldsComplete(d);
                return d;
            });
            return e;
        });
        const programCopy = JSON.parse(JSON.stringify(program)) as Program;
        programCopy.days = programCopy.days.map(d =>
            d.id === dayCopy.id ? dayCopy : d
        );
        const programRes = await ProgramService.updateProgram(programCopy);
        await checkForAndCreateDayRecords(athleteId, dayCopy);
        currentProgram.set(programRes);
        currentDay.set(programRes.days.find(d => d.id === day.id)!);
        athleteProgramSuccess.set('Updated');
    } catch (e) {
        console.log(e);
        if (
            e instanceof ExerciseMaxWeightError ||
            e instanceof ExerciseMaxRepsError
        ) {
            athleteProgramError.set(e);
        } else {
            athleteProgramError.set(e as Error);
        }
    } finally {
        athleteProgramLoading.set(false);
    }
};

const markExerciseCompleteAsWritten = async (
    exercise: Exercise,
    day: Day,
    program: Program,
    athleteId: string
) => {
    athleteProgramLoading.set(true);
    athleteProgramSuccess.set('');
    athleteProgramError.set(undefined);
    let updated: Exercise;
    try {
        let exerciseCopy = JSON.parse(JSON.stringify(exercise)) as Exercise;
        exerciseCopy = markExerciseFieldsComplete(exerciseCopy);
        exerciseCopy.dropSets.forEach(d => {
            d = markExerciseFieldsComplete(d);
            return d;
        });
        updated = await ProgramService.updateExercise(exerciseCopy);
        if (updated.type === ExerciseType.EXERCISE) {
            await checkForAndCreateAthleteRecord(athleteId, updated, day);
        }

        updateProgramStoreAfterExerciseSave(program, day, updated);
        athleteProgramSuccess.set(
            'Saved ' + updated.nameArr.length
                ? updated.nameArr.join(' + ')
                : updated.name
        );
    } catch (e: any) {
        if (
            e instanceof ExerciseMaxRepsError ||
            e instanceof ExerciseMaxWeightError
        ) {
            athleteProgramError.set(e);
        } else if (e instanceof AthleteRecordError) {
            if (updated !== undefined) {
                updateProgramStoreAfterExerciseSave(program, day, updated);
            }
            athleteProgramError.set(e);
        } else {
            console.log(e);
            e.message = `An unknown error occurred while trying to mark ${exercise.name} as complete`;
            athleteProgramError.set(e);
        }
    } finally {
        athleteProgramLoading.set(false);
    }
};

const updateProgramStoreAfterExerciseSave = (
    program: Program,
    day: Day,
    exercise: Exercise
) => {
    day.exercises = day.exercises.map(e =>
        e.id === exercise.id ? exercise : e
    );
    program.days = program.days.map(d => (d.id === day.id ? day : d));
    currentProgram.set(program);
    currentDay.set(day);
};

const checkForAndCreateAthleteRecord = async (
    athleteId: string,
    exercise: Exercise,
    day: Day
) => {
    // TODO: it may make more sense to just save a list of records every time and remove the endpoint for creating a single record
    if (exercise.type !== ExerciseType.EXERCISE) {
        return;
    }
    if (exercise.dropSets.length) {
        const records: AthleteRecord[] = [];

        const record = AthleteRecord.buildRecord(exercise, day, athleteId);
        records.push(record);
        exercise.dropSets.forEach(d => {
            const rec = AthleteRecord.buildRecord(exercise, day, athleteId);
            records.push(rec);
        });
        try {
            const newRecords = await UserService.createAthleteRecords(
                athleteId,
                records
            );
            if (newRecords.length) {
                newRecordExerciseIds.update(prev => {
                    records.forEach(r => prev.push(r.exerciseId));
                    return prev;
                });
            }
        } catch (e) {
            console.log(e);
            throw new AthleteRecordError(
                `An unknown error occurred while checking if ${exercise.name} was a personal record`
            );
        }
    } else {
        const record = AthleteRecord.buildRecord(exercise, day, athleteId);

        try {
            const newRecord = await UserService.createAthleteRecord(
                athleteId,
                record
            );
            if (newRecord) {
                newRecordExerciseIds.update(prev => {
                    prev = [...prev, record.exerciseId];
                    return prev;
                });
            }
        } catch (e) {
            console.log(e);
            throw new AthleteRecordError(
                `An unknown error occurred while checking if ${exercise.name} was a personal record`
            );
        }
    }
};

const checkForAndCreateDayRecords = async (athleteId: string, day: Day) => {
    const records: AthleteRecord[] = [];
    for (const ex of day.exercises) {
        if (ex.type !== ExerciseType.EXERCISE) continue;

        const record = AthleteRecord.buildRecord(ex, day, athleteId);
        records.push(record);
        ex.dropSets.forEach(d => {
            const rec = AthleteRecord.buildRecord(d, day, athleteId);
            records.push(rec);
        });
    }

    try {
        const newRecords = await UserService.createAthleteRecords(
            athleteId,
            records
        );
        if (newRecords && newRecords.length) {
            newRecordExerciseIds.update(prev => {
                records.forEach(r => {
                    prev.push(r.exerciseId);
                });
                return prev;
            });
        }
    } catch (e) {
        console.log(e);
        throw new AthleteRecordError(
            'An unknown error occurred while checking for personal records'
        );
    }
};

const skipExercise = async (exercise: Exercise, day: Day, program: Program) => {
    athleteProgramError.set(undefined);
    athleteProgramSuccess.set('');
    athleteProgramLoading.set(true);
    try {
        let exerciseCopy = JSON.parse(JSON.stringify(exercise)) as Exercise;
        exerciseCopy = markExerciseFieldsSkipped(exerciseCopy);
        exerciseCopy.dropSets.forEach(d => (d = markExerciseFieldsSkipped(d)));
        day.exercises = day.exercises.map(e =>
            e.id === exerciseCopy.id ? exerciseCopy : e
        );
        program.days = program.days.map(d => (d.id === day.id ? day : d));
        const programRes = await ProgramService.updateProgram(program);
        currentProgram.set(programRes);
        currentDay.set(programRes.days.find(d => d.id === day.id)!);
        athleteProgramSuccess.set(
            'Skipped ' + exercise.nameArr.length
                ? exercise.nameArr.join(' + ')
                : exercise.name
        );
    } catch (e: any) {
        console.log(e);
        e.message = 'An error occurred trying to skip ' + exercise.name;
        athleteProgramError.set(e as Error);
    } finally {
        athleteProgramLoading.set(false);
    }
};

const markExerciseFieldsSkipped = (e: Exercise) => {
    e.isComplete = true;
    e.weightCompleted = 0;
    e.setsCompleted = 0;
    if (e.type === ExerciseType.EXERCISE) {
        e.totalRepsCompleted = 0;
    } else if (e.type === ExerciseType.COMPLEX) {
        e.repCompletedArr = e.repArr.map(r => 0);
    } else if (e.type === ExerciseType.ACCESSORY) {
        e.totalRepsCompleted = 0;
    }
    return e;
};

const markExerciseFieldsComplete = (e: Exercise) => {
    if (e.isMax && !e.weightCompleted) {
        throw new ExerciseMaxWeightError(
            `${e.name} is a rep max, and requires you to enter a weight before saving`,
            e.id
        );
    }
    if (e.isMaxReps && !e.totalRepsCompleted) {
        throw new ExerciseMaxRepsError(
            `${e.name} is an AMRAP exercise, and requires you to enter the number of reps you completed before saving`,
            e.id
        );
    }
    e.isComplete = true;
    e.weightCompleted = e.weightCompleted ? e.weightCompleted : e.weight;
    e.setsCompleted = e.setsCompleted ? e.setsCompleted : e.sets;
    if (e.type === ExerciseType.EXERCISE) {
        e.totalRepsCompleted = e.repsPerSetComplete
            ? e.repsPerSetComplete * e.setsCompleted
            : e.repsPerSet * e.setsCompleted;
    } else if (e.type === ExerciseType.COMPLEX) {
        e.repCompletedArr = [...e.repArr];
    }
    if (e.type === ExerciseType.ACCESSORY) {
        e.totalRepsCompleted = e.repsPerSetComplete
            ? e.repsPerSetComplete * e.setsCompleted
            : e.repsPerSet * e.setsCompleted;
        e.actualIntesity =
            e.actualIntesity !== null ? e.actualIntesity : e.effortIntensity;
    }
    return e;
};

export const athleteProgramContext = {
    getCurrentProgram: () => currentProgram,
    getCurrentDay: () => currentDay,
    getCurrentDayIdx: () => currentDayIdx,
    getAthleteProgramLoading: () => athleteProgramLoading,
    getAthleteProgramError: () => athleteProgramError,
    getAthleteProgramSuccess: () => athleteProgramSuccess,
    getAthleteId: () => athleteId,
    getNewRecordExerciseIds: () => newRecordExerciseIds,
    markDayCompleteAsWritten,
    markExerciseCompleteAsWritten,
    skipExercise,
};
