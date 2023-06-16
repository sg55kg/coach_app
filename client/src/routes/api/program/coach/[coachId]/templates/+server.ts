import type {RequestHandler} from "@sveltejs/kit";


export const GET: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken');
    const coachId = event.params.coachId;

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}/templates`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token }
    });

    return new Response(res.body, { status: res.status, statusText: res.statusText });
}