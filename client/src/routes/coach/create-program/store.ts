import {writable} from "svelte/store";
import {Program} from "../../../lib/classes/program";


export const program = writable(new Program())