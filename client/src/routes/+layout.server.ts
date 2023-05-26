import type { LayoutServerLoad } from './$types';
import jwtDecode from 'jwt-decode';
import { error, redirect } from '@sveltejs/kit';
import { _fetchUser } from '../lib/hooks.server';

export const prerender = false;
const randomString = (length: number, chars: string) => {
    let result = '';
    for (let i = length; i > 0; --i)
        result += chars[Math.floor(Math.random() * chars.length)];
    return result;
};
// we need to encode state with base64, and then check after redirect if the state matches when decoded
const state: string = randomString(
    32,
    '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
);

export const load: LayoutServerLoad = async ({
    cookies,
    params,
    url,
    locals,
}) => {
    const encoded = state;
    const accessToken = cookies.get('accessToken');
    const idToken = cookies.get('idToken');
    if (accessToken && idToken && locals.userData) {
        return {
            user: jwtDecode(idToken),
            userData: locals.userData,
            redirectUri:
                import.meta.env.VITE_REDIRECT_URI +
                (locals.lastPage ? locals.lastPage : ''),
            baseUrl: import.meta.env.VITE_AUTH0_LOGIN_URL,
        };
    }
    if (accessToken && idToken && !locals.userData) {
        try {
            const user = jwtDecode(idToken);
            const userData = await _fetchUser(user, accessToken);
            locals.userData = userData;
            return { user, userData };
        } catch (e) {
            cookies.delete('accessToken', { path: '/' });
            cookies.delete('idToken', { path: '/' });
            throw redirect(307, '/');
        }
    } else {
        return {
            state: encoded,
            redirectUri:
                import.meta.env.VITE_REDIRECT_URI +
                (locals.lastPage ? locals.lastPage : ''),
            baseUrl: import.meta.env.VITE_AUTH0_LOGIN_URL,
        };
    }
};
