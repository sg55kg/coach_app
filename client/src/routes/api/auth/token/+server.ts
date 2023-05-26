import type {RequestHandler} from "@sveltejs/kit";
import jwtDecode from "jwt-decode";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const { code } = await event.request.json()
    if (!code) {
        throw error(405, 'Invalid code provided')
    }
    try {
        const { accessToken, user, idToken } = await _fetchToken(code)
        event.cookies.set('accessToken', accessToken, {httpOnly: true, path: '/'})
        event.cookies.set('idToken', idToken, {httpOnly: true, path: '/'})
        const userData = await _fetchUser(user, accessToken)
        return new Response(JSON.stringify({ user, userData }))
    } catch (e) {
        throw error(401, 'Could not validate')
    }
}

export const _fetchToken = async (code: string) => {
    const options = {
        "grant_type": "authorization_code",
        "client_id": import.meta.env.VITE_AUTH0_CLIENT_ID,
        "client_secret": import.meta.env.VITE_AUTH0_CLIENT_SECRET,
        "code": code,
        "redirect_uri": import.meta.env.VITE_REDIRECT_URI,
        "audience": import.meta.env.VITE_AUTH0_AUDIENCE,
    }

    try {
        let res = await fetch(import.meta.env.VITE_AUTH0_TOKEN_URL, {
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(options)
        })

        const data = await res.json()

        const user = jwtDecode(data['id_token']) as any
        const accessToken = data['access_token']
        const idToken = data['id_token']

        return { accessToken, user, idToken }
    } catch (e) {
        throw error(500, 'Could not fetch tokens')
    }

}


export const _fetchUser = async (user: any, token: string) => {
    if (!user?.email || !user?.name) {
        throw error(405, 'Invalid ID token')
    }
    try {
        let userRes = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${user.email}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            }
        })

        if (userRes.status === 404) {
            userRes = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: user.email, name: user.name, photoUrl: user.picture, preferences: { weight: 'kg', mode: 'dark' } })
            })

        }

        const userData = await userRes.json()
        // TODO: After this has run for a week or 2 remove
        if (!userData.photoUrl) {
            const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/${userData.id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: user.email, username: user.name, photoUrl: user.picture })
            })
        }
        return userData
    } catch (e: any) {
        throw error(404, 'Could not fetch user data')
    }
}