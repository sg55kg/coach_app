
import type {LayoutServerLoad} from './$types';
import jwtDecode from "jwt-decode";
import {error, redirect} from "@sveltejs/kit";
import {_fetchUser} from "./api/auth/token/+server";


export const prerender = false
const randomString = (length: number, chars: string) => {
    let result = '';
    for (let i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}
// we need to encode state with base64, and then check after redirect if the state matches when decoded
const state: string = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');


export const load: LayoutServerLoad = async ({ cookies, params, url, locals }) => {
    const encoded = state
    if (!cookies.get('accessToken')) {
        if (url && url.pathname !== '/') {
            //throw redirect(307, '/')
        }
        return { state: encoded }
    }
    if (cookies.get('accessToken') && cookies.get('idToken')) {
        try {
            const user = jwtDecode(cookies.get('idToken')!)
            const userData = await _fetchUser(user, cookies.get('accessToken')!)
            console.log(userData)
            return { user, userData }
        } catch (e) {
            cookies.delete('accessToken', { path: '/'})
            cookies.delete('idToken', { path: '/'})
            throw redirect(307, '/')
        }
    }
};


