import {writable} from "svelte/store";
import type {Writable} from "svelte/store";
import type {Auth0Client, User} from "@auth0/auth0-spa-js";

export const user: Writable<User | undefined> = writable(undefined)
export const userId: Writable<string> = writable('')
export const auth0Client: Writable<Auth0Client | null> = writable(null)
export const isAuthenticated: Writable<boolean> = writable(false)
export const loadingAuth: Writable<boolean> = writable(false)
export const userDB: Writable<any> = writable(null)