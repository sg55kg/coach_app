import type {Program, Exercise} from "$lib/classes/program";
import type {Writable} from "svelte/store";
import {writable} from "svelte/store";
import type {Day} from "$lib/classes/day";


export const currentProgram: Writable<Program | null> = writable(null)
export const currentDay: Writable<Day | null> = writable(null)
export const incompleteExercises: Writable<Exercise[]> = writable([])
export const completedExercises: Writable<Exercise[]> = writable([])