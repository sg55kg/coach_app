import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const comment = await event.request.json()
    const id = await event.params.exerciseId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${id}/comment`, {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(comment)
        })
        return new Response(res.body, { status: res.status, statusText: res.statusText })
    } catch (e) {
        throw error(405, 'Could not update comment')
    }
}