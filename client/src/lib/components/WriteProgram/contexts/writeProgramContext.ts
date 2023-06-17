import type {Program} from "../../../classes/program";
import {Day} from "../../../classes/program/day";
import type {Exercise} from "../../../classes/program/exercise";
import type {Dayjs} from "dayjs";
import {ProgramService} from "../../../service/ProgramService";
import type { Writable } from 'svelte/store';
import {get, writable} from "svelte/store";

export const writeProgramContext = (selectedProgram: Program) => {

    const program: Writable<Program> = writable(selectedProgram);
    const selectedDayIdx: Writable<number> = writable(-1);
    const selectedExerciseIdx: Writable<number> = writable(-1);
    const exerciseClipboard: Writable<Exercise[]> = writable([]);
    const dayClipboard: Writable<Day[]> = writable([]);
    const programError: Writable<string> = writable('');
    const programSuccess: Writable<string> = writable('');
    const programLoading: Writable<boolean> = writable(false);
    const programInfo: Writable<string> = writable('');

    const formatProgramDates = () => {
        let programVal = get(program);
        let currentDate: Dayjs = programVal.startDate;
        program.update(prev => {
            prev.days.forEach(d => {
                d.date = currentDate;
                currentDate = currentDate.add(1, 'day');
                return d;
            });
            prev.endDate = currentDate.subtract(1, 'day');
            return prev;
        })
    };

    const updateProgram = async () => {
        programLoading.set(true);
        programSuccess.set('');
        programError.set('');
        const currentProgram = get(program);
        try {
            const res = await ProgramService.updateProgram(currentProgram);
            program.set(res);
            programSuccess.set('Updated');
        } catch (e) {
            programError.set(`There was an error while updating ${currentProgram.name}`);
        } finally {
            programLoading.set(false);
        }
    };

    const insertDayLeft = (idx: number) => {
        const dayToInsert = new Day();
        let updatedDays: Day[] = [];
        const currentProgram = get(program);
        currentProgram.days.forEach((d, i) => {
            if (idx === 0) {
                updatedDays.push(dayToInsert);
            }
            updatedDays.push(d);
            if (i === idx - 1 && idx > 0) {
                updatedDays.push(dayToInsert);
            }
        });
        program.update(prev => {
            prev.days = updatedDays;
            return prev;
        });
        const dayCard = document.getElementById(`day-card-${idx}`);
        if (dayCard) {
            dayCard.classList.remove('selected-day');
        }
        formatProgramDates();
    };

    const insertDayRight = (idx: number) => {
        const dayToInsert = new Day();
        let updatedDays: Day[] = [];
        const currentProgram = get(program);
        if (idx === currentProgram.days.length - 1) {
            updatedDays = [...currentProgram.days, dayToInsert];
        } else {
            currentProgram.days.forEach((d, i) => {
                if (i === idx + 1) {
                    updatedDays.push(dayToInsert);
                }
                updatedDays.push(d);
            });
        }
        program.update(prev => {
            prev.days = updatedDays;
            return prev;
        });
        const dayCard = document
            .getElementById(`day-card-${idx}`);
        if (dayCard) {
            dayCard.classList.remove('selected-day');
        }
        formatProgramDates();
    };

    const clearExercises = (idx: number) => {
        program.update(prev => {
            prev.days[idx].exercises = [];
            return prev;
        })
    };

    const copyDay = (idx: number) => {
        const currentProgram = get(program);
        const day = currentProgram.days[idx];
        const dayCopy = day.copy();
        dayClipboard.update(prev => {
            prev = [dayCopy, ...prev];
            return prev;
        })
        const dayCard = document.getElementById(`day-card-${idx}`);

        if (dayCard) {
            dayCard.classList.remove('selected-day');
        }
    };

    const pasteDay = (idx: number) => {
        const clipboard = get(dayClipboard);
        const dayCard = document.getElementById(`day-card-${idx}`);
        if (clipboard.length < 1) {
            if (dayCard) {
                dayCard.classList.remove('selected-day');
            }
            return;
        }
        const currentProgram = get(program);
        const id = currentProgram.days[idx].id;
        const dayToPaste = clipboard.shift() as Day;
        dayClipboard.set(clipboard);
        program.update(prev => {
            prev.days[idx] = dayToPaste;
            prev.days[idx].id = id;
            return prev;
        });
        if (dayCard) {
            dayCard.classList.remove('selected-day');
        }
        formatProgramDates();
    };

    const deleteExercise = async (exercise: Exercise) => {
        programLoading.set(true);
        programSuccess.set('');
        programError.set('');
        try {
            await ProgramService.deleteExercise(exercise);
        } catch (e) {
            console.log(e);
            programError.set('There was an error trying to delete this exercise');
        } finally {
            programLoading.set(false);
        }
    };

    const addDay = () => {
        const day = new Day();
        program.update(prev => {
            prev.days.push(day);
            return prev;
        })
        formatProgramDates();
    };

    const copyExercise = (index: number) => {
        const dayIndex = get(selectedDayIdx);
        const currentProgram = get(program);
        if (dayIndex < 0) {
            return;
        }
        const exerciseCopy = currentProgram.days[dayIndex].exercises[index].copy();
        exerciseClipboard.update(prev => {
            prev.unshift(exerciseCopy);
            return prev;
        })
    };

    const pasteExercise = () => {
        const dayIndex = get(selectedDayIdx);
        const exerciseIndex = get(selectedExerciseIdx);
        const clipboard = get(exerciseClipboard);
        const currentProgram = get(program);
        if (dayIndex < 0) {
            return;
        }
        const exerciseToPaste = clipboard.shift() as Exercise;
        exerciseClipboard.set(clipboard);
        if (exerciseIndex > -1) {
            const id = currentProgram.days[dayIndex].exercises[exerciseIndex].id;
            program.update(prev => {
                prev.days[dayIndex].exercises[exerciseIndex] = exerciseToPaste;
                prev.days[dayIndex].exercises[exerciseIndex].id = id;
                return prev;
            })
        } else {
            program.update(prev => {
                prev.days[dayIndex].exercises.push(exerciseToPaste);
                return prev;
            })
        }
    }
    return {
        getProgram: () => program,
        getSelectedDayIdx: () => selectedDayIdx,
        getSelectedExerciseIdx: () => selectedExerciseIdx,
        getExerciseClipboard: () => exerciseClipboard,
        getDayClipboard: () => dayClipboard,
        getProgramError: () => programError,
        getProgramSuccess: () => programSuccess,
        getProgramLoading: () => programLoading,
        updateProgram,
        formatProgramDates,
        insertDayLeft,
        insertDayRight,
        clearExercises,
        copyDay,
        pasteDay,
        deleteExercise,
        addDay,
        copyExercise,
        pasteExercise,
        getProgramInfo: () => programInfo
    };
}
