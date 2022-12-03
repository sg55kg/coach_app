import type {Actions} from "@sveltejs/kit";
import axios from "axios";
import {invalid, redirect} from "@sveltejs/kit";
import type {PageServerLoad} from "./$types";


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
            return invalid(404, { error: 'Could not login' })
        }

        if (result.token['access_token'] === undefined || result.token['access_token'].includes('undefined')) {
            return invalid(500, { error: 'Token not found' })
        }

        cookies.set('Authority', `${result.token['access_token']}`, {
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
        console.log(result)
        const userId = result.userData.id
        console.log(result.userData)
        const location = result
            .userData
            .roles.find((r: any) => r.name.includes('coach')) ? `/coach/${userId}` : `/athlete/${userId}`
        return { success: result.userData, location, status: 302 }
        //throw redirect(302, location)
    }
}