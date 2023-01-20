import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const GET: RequestHandler = async ({ request, fetch, cookies, params }) => {
    const token = cookies.get('accessToken')
    const programId = params.programId
    if (!token) {
        throw error(401, 'Invalid token')
    }
    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + token}
        })
        const programRes = await res.json()
        return new Response(JSON.stringify(programRes))
    } catch (e) {
        throw error(500)
    }
}