import {writable} from "svelte/store";
import {Program} from "../classes/program";


export const program = writable(new Program())