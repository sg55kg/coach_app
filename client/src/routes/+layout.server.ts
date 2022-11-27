import type {LayoutServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";


export const load: LayoutServerLoad = async ({ url, cookies }) => {
    if(cookies.get('Authority') !== undefined && cookies.get('Authority')!.includes('undefined') && !url.pathname.includes('login')) {
        throw redirect(302, '/login')
    }
    if (!cookies.get('Authority') && cookies.get('refresh_token')) {
        console.log('fired')
        const res = await fetch(`http://localhost:8180/auth/refresh`, {
            method: 'POST',
            body: cookies.get('refresh_token')
        })
        console.log(res)
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
    }

}