import type {RequestHandler} from "@sveltejs/kit";
import {error, redirect} from "@sveltejs/kit";


export const GET: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const athleteId = event.params.athleteId

    if (!token) {
        throw redirect(307, '/')
    }

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/stats`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
        const data = await res.json()
        return new Response(JSON.stringify(data))
    } catch (e) {
        throw error(404, 'Stats not found')
    }
}