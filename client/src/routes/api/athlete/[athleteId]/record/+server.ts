import type {RequestHandler} from "@sveltejs/kit";
import {error, redirect} from "@sveltejs/kit";


export const PUT: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const record = await event.request.json()
    const id = event.params.athleteId

    if (!token) {
        throw redirect(307, '/')
    }
    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${id}/record`, {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(record)
        })
        const updatedRecord = await res.json()
        return new Response(JSON.stringify(updatedRecord))
    } catch (e) {
        throw error(405, 'Could not update record')
    }
}

export const GET: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const athleteId = event.params.athleteId

    if (!token) {
        throw redirect(307, '/')
    }
    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/record`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
        const records = await res.json()
        return new Response(JSON.stringify((records)))
    } catch (e) {
        throw error(404, 'Records not found')
    }
}