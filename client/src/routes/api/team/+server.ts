import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const team = await event.request.text();

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + token,
        },
        body: team,
    });
    return new Response(await res.text(), { status: res.status, statusText: res.statusText, headers: res.headers });
};

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams/`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + token,
        },
    });
    return new Response(await res.text(), { status: res.status, statusText: res.statusText, headers: res.headers });
};
