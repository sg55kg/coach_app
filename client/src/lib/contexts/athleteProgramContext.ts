
import type {Writable} from "svelte/store";
import {Program} from "../classes/program";
import {writable} from "svelte/store";
import type {Day} from "../classes/program/day";
import {ExerciseType} from "../classes/program/exercise/enums";
import {ProgramService} from "../service/ProgramService";
import type {Exercise} from "../classes/program/exercise";

export class ExerciseMaxWeightError extends Error {
    constructor(message: string, id: string) {
        super();
        this.message = message
        this.id = id
    }
    id: string = ''
}

export class ExerciseMaxRepsError extends Error {
    constructor(message: string, id: string) {
        super();
        this.message = message
        this.id = id
    }
    id: string = ''
}

let currentProgram: Writable<Program> = writable(new Program())
let currentDay: Writable<Day | undefined> = writable(undefined)
let currentDayIdx: Writable<number> = writable(-1)
let athleteProgramLoading: Writable<boolean> = writable(false)
let athleteProgramSuccess: Writable<string> = writable('')
let athleteProgramError: Writable<Error | undefined> = writable()

const markDayCompleteAsWritten = async (day: Day, program: Program) => {
    athleteProgramLoading.set(true)
    athleteProgramSuccess.set('')
    athleteProgramError.set(undefined)
    try {
        const dayCopy = JSON.parse(JSON.stringify(day)) as Day
        dayCopy.exercises.forEach(e => {
            e = markExerciseFieldsComplete(e)
            e.dropSets.forEach(d => {
                d = markExerciseFieldsComplete(d)
                return d
            })
            return e
        })
        const programCopy = JSON.parse(JSON.stringify(program)) as Program
        programCopy.days = programCopy.days.map(d => d.id === dayCopy.id ? dayCopy : d)
        const programRes = await ProgramService.updateProgram(programCopy)

        currentProgram.set(programRes)
        currentDay.set(programRes.days.find(d => d.id === day.id)!)
        athleteProgramSuccess.set('Updated')
    } catch (e) {
        console.log(e)
        if (e instanceof ExerciseMaxWeightError || e instanceof ExerciseMaxRepsError) {
            athleteProgramError.set(e)
        } else {
            athleteProgramError.set(e as Error)
        }
    } finally {
        athleteProgramLoading.set(false)
    }
}

const markExerciseCompleteAsWritten = async (exercise: Exercise, day: Day, program: Program) => {
    athleteProgramLoading.set(true)
    athleteProgramSuccess.set('')
    athleteProgramError.set(undefined)
    try {
        const exerciseCopy = JSON.parse(JSON.stringify(exercise)) as Exercise
        // exerciseCopy.weightCompleted = ex
    } catch (e: any) {
        if (e instanceof ExerciseMaxRepsError || e instanceof ExerciseMaxWeightError) {
            athleteProgramError.set(e)
        } else {
            e.message = 'An unknown error occurred while trying to mark this day as complete'
            athleteProgramError.set(e)
        }
    }
}

const skipExercise = async (exercise: Exercise, day: Day, program: Program) => {
    athleteProgramError.set(undefined)
    athleteProgramSuccess.set('')
    athleteProgramLoading.set(true)
    try {
        let exerciseCopy = JSON.parse(JSON.stringify(exercise)) as Exercise
        exerciseCopy = markExerciseFieldsSkipped(exerciseCopy)
        exerciseCopy.dropSets.forEach(d => d = markExerciseFieldsSkipped(d))
        day.exercises = day.exercises.map(e => e.id === exerciseCopy.id ? exerciseCopy : e)
        program.days = program.days.map(d => d.id === day.id ? day : d)
        const programRes = await ProgramService.updateProgram(program)
        currentProgram.set(programRes)
        currentDay.set(programRes.days.find(d => d.id === day.id)!)
        athleteProgramSuccess.set('Skipped ' + exercise.name)
    } catch (e: any) {
        console.log(e)
        e.message = 'An error occurred trying to skip ' + exercise.name
        athleteProgramError.set(e as Error)
    } finally {
        athleteProgramLoading.set(false)
    }
}

const markExerciseFieldsSkipped = (e: Exercise) => {
    e.isComplete = true
    e.weightCompleted = 0
    e.setsCompleted = 0
    if (e.type === ExerciseType.EXERCISE) {
        e.totalRepsCompleted = 0
    } else if (e.type === ExerciseType.COMPLEX) {
        e.repCompletedArr = e.repArr.map(r => 0)
    }
    return e
}

const markExerciseFieldsComplete = (e: Exercise) => {
    if (e.isMax && !e.weightCompleted) {
        throw new ExerciseMaxWeightError(`${e.name} is a rep max, and requires you to enter a weight before saving`, e.id)
    }
    if (e.isMaxReps && !e.totalRepsCompleted) {
        throw new ExerciseMaxRepsError(`${e.name} is an AMRAP exercise, and requires you to enter the number of reps you completed before saving`, e.id)
    }
    e.isComplete = true
    e.weightCompleted = e.weightCompleted ? e.weightCompleted : e.weight
    e.setsCompleted = e.setsCompleted ? e.setsCompleted : e.sets
    if (e.type === ExerciseType.EXERCISE) {
        e.totalRepsCompleted = e.repsPerSetComplete ? (e.repsPerSetComplete * e.setsCompleted) : (e.repsPerSet * e.setsCompleted)
    } else if (e.type === ExerciseType.COMPLEX) {
        e.repCompletedArr = [...e.repArr]
    }
    return e
}

export const athleteProgramContext = {
    getCurrentProgram: () => currentProgram,
    getCurrentDay: () => currentDay,
    getCurrentDayIdx: () => currentDayIdx,
    getAthleteProgramLoading: () => athleteProgramLoading,
    getAthleteProgramError: () => athleteProgramError,
    getAthleteProgramSuccess: () => athleteProgramSuccess,
    markDayCompleteAsWritten,
    skipExercise
}
