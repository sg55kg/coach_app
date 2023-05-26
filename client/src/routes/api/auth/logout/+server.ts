import type { RequestHandler } from '@sveltejs/kit';
import { json, redirect } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    event.cookies.delete('accessToken', { path: '/' });
    event.cookies.delete('idToken', { path: '/' });
    return json({
        status: 200,
        redirectUrl: import.meta.env.VITE_AUTH0_LOGOUT_URL,
    });
};
