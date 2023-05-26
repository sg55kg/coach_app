import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';
import { Program } from '$lib/classes/program';
import type { Exercise } from '$lib/classes/program/exercise';
import type { Day } from '$lib/classes/program/day';

export const program: Writable<Program> = writable(new Program());
export const programError: Writable<string> = writable('');
export const programSuccess: Writable<string> = writable('');
export const exerciseClipboard: Writable<Exercise[]> = writable([]);
export const dayClipboard: Writable<Day[]> = writable([]);
