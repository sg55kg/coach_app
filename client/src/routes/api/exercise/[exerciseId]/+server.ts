import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const PUT: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const exercise = await event.request.json()
    const id = event.params.exerciseId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(exercise)
        })
        const exerciseData = await res.json()

        return new Response(JSON.stringify(exerciseData))
    } catch (e) {
        throw error(405, 'Could not update exercise')
    }
}

export const DELETE: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const id = event.params.exerciseId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' }
        })
        return new Response(null, { status: 204 })
    } catch (e) {
        throw error(405, 'Could not delete exercise')
    }
}