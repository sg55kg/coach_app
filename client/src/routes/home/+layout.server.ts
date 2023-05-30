import type { LayoutServerLoad } from './$types';
import jwtDecode from 'jwt-decode';

export const prerender = false;

export const load: LayoutServerLoad = async event => {
    const accessToken = event.cookies.get('accessToken');
    const idToken = event.cookies.get('idToken');
    if (accessToken && idToken && event.locals.userData) {
        return {
            user: jwtDecode(idToken),
            userData: event.locals.userData,
            redirectUri:
                import.meta.env.VITE_REDIRECT_URI +
                (event.locals.lastPage ? event.locals.lastPage : ''),
            baseUrl: import.meta.env.VITE_AUTH0_LOGIN_URL,
        };
    }
};
