import type {LayoutServerLoad} from "./$types";
import {invalid, redirect} from "@sveltejs/kit";


export const load: LayoutServerLoad = async ({ url, cookies }) => {
    const accessToken = cookies.get('Authority')
    const refreshToken = cookies.get('refresh_token')
    if(accessToken !== undefined && accessToken.includes('undefined') &&
        !url.pathname.includes('login') &&
        !url.pathname.includes('register')
    ) {
        throw redirect(302, '/login')
    }
    if (!accessToken && refreshToken) {
        console.log('fired here')
        try {
            const res = await fetch(`http://localhost:8180/auth/refresh`, {
                method: 'POST',
                headers: new Headers({ 'content-type': 'application/json' }),
                body: refreshToken
            })
            //console.log(res)
            const tokenData = await res.json()
            console.log(tokenData)
            cookies.set('Authority', `Bearer ${tokenData['access_token']}`, {
                path: '/',
                httpOnly: true,
                sameSite: 'strict',
                secure: process.env.NODE_ENV === 'production',
                maxAge: tokenData['expires_in']
            })
            cookies.set('refresh_token', tokenData['refresh_token'], {
                path: '/',
                httpOnly: true,
                sameSite: 'strict',
                secure: process.env.NODE_ENV === 'production',
                maxAge: tokenData['refresh_expires_in']
            })
            return {}
        } catch (e) {
            return { error: e }
        }

    }

}