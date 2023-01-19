import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const program = await event.request.json()
    const coachId = program.coachId

    try {
        let res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`, {
            method: 'POST',
            body: JSON.stringify(program),
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' }
        })
        const programData = await res.json()
        return new Response(JSON.stringify(programData))
    } catch (e) {
        throw error(405, 'Could not create new team')
    }
}