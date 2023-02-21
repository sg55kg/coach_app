import type {Writable} from "svelte/store";
import {writable} from "svelte/store";


export const loadingChat: Writable<boolean> = writable(false)
export const chatError: Writable<string> = writable('')
