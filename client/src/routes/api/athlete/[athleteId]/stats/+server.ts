import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const athleteId = event.params.athleteId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/stats`,
        {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
        }
    );
    return new Response(await res.text(), {
        status: res.status,
        statusText: res.statusText,
    });
};
