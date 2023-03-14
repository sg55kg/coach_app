import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const GET: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    const chatId = event.params.chatId

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/chat-rooms/${chatId}`, {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' }
        })
        return new Response(res.body, {status: res.status, statusText: res.statusText })
    } catch (e) {
        throw error(404, 'Chat not found')
    }
}