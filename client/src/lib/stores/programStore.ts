import {writable} from "svelte/store";
import type {Writable} from "svelte/store";
import {Program} from "../classes/program";


export const program = writable(new Program())
export const displayPrograms: Writable<Program[]> = writable([])
export const programError: Writable<string> = writable('')