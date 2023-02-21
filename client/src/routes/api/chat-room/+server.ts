import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const teamReq = await event.request.json()

    try {
        const res = await event.fetch('http://localhost:8180/api/chat-rooms/', {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
            body: JSON.stringify(teamReq)
        })
        const chatRoom = await res.json()
        return new Response(JSON.stringify(chatRoom))
    } catch (e) {
        throw error(405)
    }
}