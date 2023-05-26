import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const id = event.params.teamId;

    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + token,
        },
    });
};

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const team = await event.request.text();
    const teamId = event.params.teamId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/teams/${teamId}`,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + token,
            },
            body: team,
        }
    );
};
