import type {Actions} from "@sveltejs/kit";

import {user} from "../../lib/stores/authStore";
import {deserialize} from "$app/forms";
import axios from "axios";
import {redirect} from "@sveltejs/kit";




export const actions: Actions = {
    default: async ({ request , cookies }) => {
        const data = await request.formData()
        let result

        const username = await data.get('username')
        const password = await data.get('password')
        console.log(username, password)
        try {
            // const res = await fetch(`http://localhost:8180/auth/login`, {
            //     method: 'POST',
            //     headers: {
            //         'Content-Type': 'application/json',
            //         'Accept': 'application/json'
            //     },
            //     body: JSON.stringify({
            //         username,
            //         password
            //     })
            // })
            const res = await axios.post(`http://localhost:8180/auth/login`, { username, password })
            //console.log(res)
            //const res = await UserService.loginUser(username!.toString(), password!.toString())
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
        const userId = result.userData.id
        const location = result.userData.roles.includes('coach') ? `/coach/${userId}` : `/athlete/${userId}`
        //return { success: result.userData, location, status: 302 }
        throw redirect(302, location)
    }
}