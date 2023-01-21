import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const PUT: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const athlete = await event.request.json()
    const id = event.params.athleteId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(athlete)
        })
        const athleteData = await res.json()
        return new Response(JSON.stringify(athleteData))
    } catch (e) {
        throw error(405, 'Could not update athlete data')
    }
}