import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const athlete = await event.request.text();
    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/athlete`, {
        method: 'PUT',
        headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: athlete,
    });
    return new Response(await res.text(), { status: res.status, statusText: res.statusText });
};
