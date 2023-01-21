import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const newCoach = await event.request.json()
    const token = event.cookies.get('accessToken')

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/coach`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(newCoach)
        })

        const updatedUser = await res.json()
        return new Response(JSON.stringify(updatedUser))
    } catch (e) {
        console.log(e)
        throw error(405, 'Could not save new coach data')
    }
}