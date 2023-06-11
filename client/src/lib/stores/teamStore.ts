import type { Team } from '$lib/classes/team';
import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';

export const team: Writable<Team | null> = writable(null);
