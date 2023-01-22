import type {RequestHandler} from "@sveltejs/kit";
import {error} from "@sveltejs/kit";


export const GET: RequestHandler = async (event) => {
    const id = event.params.coachId
    const token = event.cookies.get('accessToken')

    try {
        return new Response()
    } catch (e) {
        throw error(404, 'Could not retrieve coach programs')
    }
}