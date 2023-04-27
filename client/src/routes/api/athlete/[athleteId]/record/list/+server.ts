import type {RequestHandler} from "@sveltejs/kit";
import {error, redirect} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const athleteId = event.params.athleteId
    const records = await event.request.json()
    const token = event.cookies.get('accessToken')

    if (!token) {
        throw redirect(302, '/')
    }

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/records/list`, {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(records)
        })
        return new Response(res.body)
    } catch (e) {
        console.log(e)
        throw error(404, (e as any).message)
    }

}