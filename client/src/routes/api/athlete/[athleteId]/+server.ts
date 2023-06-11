import type { RequestHandler } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const athlete = await event.request.text();
    const id = event.params.athleteId;

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/athletes/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: athlete,
    });
    return new Response(await res.text(), { status: res.status, statusText: res.statusText });
};
