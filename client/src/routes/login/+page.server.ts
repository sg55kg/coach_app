import type {Actions} from "@sveltejs/kit";
import axios from "axios";
import {redirect} from "@sveltejs/kit";




export const actions: Actions = {
    default: async ({ request , cookies }) => {
        const data = await request.formData()
        let result

        const username = await data.get('username')
        const password = await data.get('password')

        try {
            const res = await axios.post(`http://localhost:8180/auth/login`, { username, password })
            result = res.data

        } catch (e: any) {
            console.log(e)
        }

        cookies.set('Authority', `Bearer ${result.token['access_token']}`, {
            path: '/',
            httpOnly: true,
            sameSite: 'strict',
            secure: process.env.NODE_ENV === 'production',
            maxAge: result.token['expires_in']
        })
        cookies.set('refresh_token', result.token['refresh_token'], {
            path: '/',
            httpOnly: true,
            sameSite: 'strict',
            secure: process.env.NODE_ENV === 'production',
            maxAge: result.token['refresh_expires_in']
        })

        const userId = result.userData.id
        console.log(result.userData)
        const location = result
            .userData
            .roles.find((r: any) => r.name.includes('coach')) ? `/coach/${userId}` : `/athlete/${userId}`
        //return { success: result.userData, location, status: 302 }
        throw redirect(302, location)
    }
}