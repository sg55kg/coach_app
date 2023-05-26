import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const id = event.params.teamId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/teams/${id}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + token,
            },
        }
    );
    console.log(res);
    const teamData = await res?.body;
    return new Response(teamData, { status: res.status });
};

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const team = await event.request.json();

    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/teams/${team.id}`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: 'Bearer ' + token,
                },
                body: JSON.stringify(team),
            }
        );
        const teamData = await res.json();
        return new Response(JSON.stringify(teamData));
    } catch (e) {
        throw error(405, 'Could not update team');
    }
};
