import type {Writable} from "svelte/store";
import {writable} from "svelte/store";
import type {Program} from "../classes/program";


export const displayPrograms: Writable<Program[]> = writable([])
export const programError: Writable<string> = writable('')