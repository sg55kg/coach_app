import type { Writable } from 'svelte/store';
import { writable } from 'svelte/store';
import type { Message } from '$lib/classes/chat';

export const loadingChat: Writable<boolean> = writable(false);
export const chatError: Writable<string> = writable('');
export const notifications: Writable<Array<Message>> = writable([]);
export const chatTimeout: Writable<string> = writable('');
