import type {Writable} from "svelte/store";
import {writable} from "svelte/store";
import {Program} from "$lib/classes/program";

export const program: Writable<Program> = writable(new Program())
export const programError: Writable<string> = writable('')
export const programSuccess: Writable<string> = writable('')