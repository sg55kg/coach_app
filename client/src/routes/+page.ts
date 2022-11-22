import type {PageServerLoad, Actions, Action} from "../../.svelte-kit/types/src/routes/sverdle/$types";
import {ProgramService} from "../lib/server/service/ProgramService";
//import {Actions} from "@sveltejs/kit";

export const load: PageServerLoad = async ({ fetch, params }) => {
   // const programs = await ProgramService.getPrograms()

   // return { programs }
    return { login }
}

export const actions: Actions = {
    login: async (event) => {

        console.log('event')
        try {
            // const res = await fetch(`http://localhost:8180/auth/login`, {
            //     username: event.params.username,
            //     password: event.params.password
            // })
        } catch (e) {
            console.log(e)
        }
    }
}

export const login: Action = async ({ cookies, request }) => {
    const data = await request.formData()
    const username = data.get('username')
    const password = data.get('password')

    const res = await fetch(`http://localhost:8081/auth/login`, {
        method: 'POST',
        body: JSON.stringify({
            username,
            password
        })

    })
    const userToken = await res.json()
    console.log(userToken)
}
