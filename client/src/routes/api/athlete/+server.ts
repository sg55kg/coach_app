import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const athlete = await event.request.json()

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/athlete`, {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(athlete)
        })
        const savedAthlete = await res.json()
        return new Response(JSON.stringify(savedAthlete))
    } catch (e) {
        throw error(405, 'Could not save athlete data')
    }
}