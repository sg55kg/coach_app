
import type {LayoutServerLoad} from './$types';
import jwtDecode from "jwt-decode";
import {error, redirect} from "@sveltejs/kit";


const randomString = (length: number, chars: string) => {
    let result = '';
    for (let i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}
const state: string = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');


export const load: LayoutServerLoad = async ({ cookies, params, url, locals }) => {
    const code = url.searchParams.get('code')
    const encoded = state
    if (!code && !cookies.get('accessToken')) {
        return { state: encoded }
    } else if (code) {
        try {
            const { accessToken, user, idToken } = await fetchToken(code)
            cookies.set('accessToken', accessToken, {httpOnly: true, path: '/'})
            cookies.set('idToken', idToken, {httpOnly: true, path: '/'})
            const userData = await fetchUser(user.email, accessToken)
            return { user, userData }
        } catch (e) {
            console.log(e)
            throw redirect(307, '/')
        }
    }
    if (cookies.get('accessToken') && cookies.get('idToken')) {
        console.log('\n\nFETCHING\n\n')
        try {
            const user = jwtDecode(cookies.get('idToken')!)
            const userData = await fetchUser(user.email, cookies.get('accessToken')!)
            return { user, userData }
        } catch (e) {
            throw redirect(307, '/')
        }
    }
};


const fetchToken = async (code: string) => {
    const options = {
        "grant_type": "authorization_code",
        "client_id": import.meta.env.VITE_AUTH0_CLIENT_ID,
        "client_secret": import.meta.env.VITE_AUTH0_CLIENT_SECRET,
        "code": code,
        "redirect_uri": import.meta.env.VITE_REDIRECT_URI,
        "audience": import.meta.env.VITE_AUTH0_AUDIENCE,
    }

    try {
        const res = await fetch(`https://dev-iubbkos4gue16ad5.us.auth0.com/oauth/token`, {
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(options)
        })
        console.log(res.status)

        const data = await res.json()

        const user = jwtDecode(data['id_token']) as any
        const accessToken = data['access_token']
        const idToken = data['id_token']

        return { accessToken, user, idToken }
    } catch (e) {
        throw error(500, 'Could not fetch tokens')
    }

}


const fetchUser = async (email: string, token: string) => {
    try {
        const userRes = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${email}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            }
        })

        console.log(userRes.status)
        const userData = await userRes.json()

        return userData
    } catch (e) {
        throw error(500, 'Could not fetch user data')
    }
}