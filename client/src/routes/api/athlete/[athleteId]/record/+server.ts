import type { RequestHandler } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const record = await event.request.text();
    const id = event.params.athleteId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/athletes/${id}/record`,
        {
            method: 'PUT',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: record,
        }
    );
    return new Response(await res.text(), {
        status: res.status,
        statusText: res.statusText,
    });
};

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const athleteId = event.params.athleteId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/record`,
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

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const athleteId = event.params.athleteId;
    const record = await event.request.text();

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/athletes/${athleteId}/records`,
        {
            method: 'POST',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: record,
        }
    );
    return new Response(await res.text(), {
        status: res.status,
        statusText: res.statusText,
    });
};
