import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const PUT: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const program = await event.request.json()

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${program.id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(program)
        })
        const programData = await res.json()
        return new Response(JSON.stringify(programData))
    } catch (e) {
        throw error(405, 'Could not update program')
    }
}

export const DELETE: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const programId = event.params.programId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        })
        return new Response('Success')
    } catch (e) {
        throw error(405, 'Could not delete program')
    }
}