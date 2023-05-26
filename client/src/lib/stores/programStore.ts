import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';
import type { DisplayProgram } from '../classes/program';

export const displayPrograms: Writable<DisplayProgram[]> = writable([]);
