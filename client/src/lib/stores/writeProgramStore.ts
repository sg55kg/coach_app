import {writable} from "svelte/types/runtime/store";
import {Program} from "$lib/classes/program";

export const program = writable(new Program())