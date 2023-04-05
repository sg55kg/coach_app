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

export const GET: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const name = event.url.searchParams.get('name')
    const coachId = event.url.searchParams.get('coach')
    try {
        let res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}/search?name=${name}`, {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' }
        })
        return new Response(res.body)
    } catch (e) {
        console.log(e)
        throw error(404, 'Could not retrieve programs')
    }
}