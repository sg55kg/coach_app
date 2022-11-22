import type {Action, Actions} from "@sveltejs/kit";
import {redirect} from "@sveltejs/kit";


export const actions: Actions = {
    default: async ({ request }) => {

        const data = await request.formData()

        try {
            const res = await fetch(`http://localhost:8180/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: data.get('username'), //username,
                    password: data.get('password'),
                    firstName: data.get('first-name'),
                    lastName: data.get('last-name'),
                    email: data.get('email'),
                    role: 'athlete'
                })

            })

        } catch (e) {
            return { error: e}
        }
        throw redirect(303, '/login')
    }
}