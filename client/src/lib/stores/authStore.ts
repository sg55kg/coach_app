import {writable} from "svelte/store";
import type {Writable} from "svelte/store";

export const user: Writable<any> = writable(null)
export const userId: Writable<string> = writable('')