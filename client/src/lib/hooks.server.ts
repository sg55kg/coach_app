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
                token,
                event.url.pathname
            );
        }
        if (!event.locals.userData) {
            event.cookies.delete('accessToken');
            event.cookies.delete('idToken');
            event.locals.lastPage = event.url.pathname;
        } else {
            return resolve(event);
        }
    }
    if (code) {
        const { accessToken, user, idToken } = await fetchToken(code);
        event.cookies.set('accessToken', accessToken, {
            httpOnly: true,
            path: '/',
        });
        event.cookies.set('idToken', idToken, { httpOnly: true, path: '/' });
        event.locals.userData = await _fetchUser(
            user,
            accessToken,
            event.url.pathname
        );
        throw redirect(302, event.locals.lastPage ?? '/home');
    } else if (!token || !event.locals.userData) {
        event.cookies.delete('accessToken');
        event.cookies.delete('idToken');
        event.locals.lastPage = event.url.pathname;
        event.locals.userData = undefined;
        if (event.url.pathname !== '/') {
            throw redirect(302, '/');
        }
    }
    // Should not reach here
    return resolve(event);
};

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    const token = event.cookies.get('accessToken');
    const idToken = event.cookies.get('idToken');
    if (token && idToken && !event.locals.userData) {
        console.log('starting fetch in handle fetch');
        const user = jwtDecode(idToken);
        event.locals.userData = await _fetchUser(
            user,
            token,
            event.url.pathname
        );
    }
    if (!token || !event.locals.userData) {
        console.log('redirecting in handle fetch');
        event.locals.userData = undefined;
        event.cookies.delete('accessToken');
        event.cookies.delete('idToken');
        event.locals.lastPage = event.url.pathname;
        if (event.url.pathname !== '/') {
            throw redirect(302, '/');
        } else {
            throw error(500, 'Unauthenticated');
        }
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
        throw redirect(302, '/');
    }
};

export const _fetchUser = async (
    user: any,
    token: string,
    currentUrl: string
) => {
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
            userRes = await createUserData(token, user);
        }

        const userData = await userRes.json();
        // TODO: After this has run for a week or 2 remove
        if (!userData.photoUrl) {
            await saveUserImage(userData.id, token, {
                email: user.email,
                username: user.name,
                photoUrl: user.picture,
            });
            userData.photoUrl = user.picture;
        }
        return userData;
    } catch (e: any) {
        console.log(e);
        if (currentUrl !== '/') {
            throw redirect(302, '/');
        } else {
            //throw error(500, 'Error fetching user data')
            return undefined;
        }
    }
};

const createUserData = async (token: string, user: any) => {
    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/users`, {
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
    });
};

const saveUserImage = async (
    id: string,
    token: string,
    body: { email: string; username: string; photoUrl: string }
) => {
    await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${id}`, {
        method: 'PUT',
        headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
};
