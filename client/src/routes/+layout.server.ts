import type {LayoutServerLoad} from "./$types";


export const load: LayoutServerLoad = async ({ cookies }) => {
    if (!cookies.get('Authority') && cookies.get('refresh_token')) {
        const res = await fetch(`http://localhost:8180/auth/refresh`, {
            method: 'POST',
            body: cookies.get('refresh_token')
        })
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