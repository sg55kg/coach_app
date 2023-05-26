import type { Handle, HandleFetch } from '@sveltejs/kit';
import { error, redirect } from '@sveltejs/kit';
import jwtDecode from 'jwt-decode';

export const handle: Handle = async ({ event, resolve }) => {
    const code = event.url.searchParams.get('code');
    const token = event.cookies.get('accessToken');
    if (token) {
        if (!event.locals.userData) {
            event.locals.userData = await _fetchUser(
                jwtDecode(event.cookies.get('idToken')!),
                token
            );
        }
        return resolve(event);
    }
    if (code) {
        const { accessToken, user, idToken } = await fetchToken(code);
        event.cookies.set('accessToken', accessToken, {
            httpOnly: true,
            path: '/',
        });
        event.cookies.set('idToken', idToken, { httpOnly: true, path: '/' });
        event.locals.userData = await _fetchUser(user, accessToken);
        throw redirect(302, event.locals.lastPage ?? '/home');
    } else if (event.url.pathname !== '/' && !token) {
        event.cookies.delete('accessToken');
        event.cookies.delete('idToken');
        event.locals.lastPage = event.url.pathname;
        event.locals.userData = undefined;
        throw redirect(302, '/');
    }
    // Should not reach here
    return resolve(event);
};

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    const token = event.cookies.get('accessToken');
    if (token && !event.locals.userData) {
        event.locals.userData = await _fetchUser(user, accessToken);
    }
    if (!token && event.url.pathname !== '/') {
        event.locals.lastPage = event.url.pathname;
        throw redirect(302, '/');
    }

    return await fetch(request);
};

const fetchToken = async (code: string) => {
    const options = {
        grant_type: 'authorization_code',
        client_id: import.meta.env.VITE_AUTH0_CLIENT_ID,
        client_secret: import.meta.env.VITE_AUTH0_CLIENT_SECRET,
        code: code,
        redirect_uri: import.meta.env.VITE_REDIRECT_URI,
        audience: import.meta.env.VITE_AUTH0_AUDIENCE,
    };

    try {
        let res = await fetch(import.meta.env.VITE_AUTH0_TOKEN_URL, {
            method: 'POST',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(options),
        });

        const data = await res.json();

        const user = jwtDecode(data['id_token']) as any;
        const accessToken = data['access_token'];
        const idToken = data['id_token'];

        return { accessToken, user, idToken };
    } catch (e) {
        throw error(500, 'Could not fetch tokens');
    }
};

export const _fetchUser = async (user: any, token: string) => {
    if (!user?.email || !user?.name) {
        throw error(405, 'Invalid ID token');
    }
    try {
        let userRes = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/users/${user.email}`,
            {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            }
        );

        if (userRes.status === 404) {
            userRes = await fetch(
                `${import.meta.env.VITE_SERVER_URL}api/users`,
                {
                    method: 'POST',
                    headers: {
                        Authorization: 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        email: user.email,
                        name: user.name,
                        photoUrl: user.picture,
                        preferences: { weight: 'kg', mode: 'dark' },
                    }),
                }
            );
        }

        const userData = await userRes.json();
        // TODO: After this has run for a week or 2 remove
        if (!userData.photoUrl) {
            const res = await fetch(
                `${import.meta.env.VITE_SERVER_URL}api/users/${userData.id}`,
                {
                    method: 'PUT',
                    headers: {
                        Authorization: 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        email: user.email,
                        username: user.name,
                        photoUrl: user.picture,
                    }),
                }
            );
        }
        return userData;
    } catch (e: any) {
        throw error(404, 'Could not fetch user data');
    }
};
