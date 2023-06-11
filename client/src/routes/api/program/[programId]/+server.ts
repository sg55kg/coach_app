import type { RequestHandler } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const program = await event.request.text();
    const programId = event.params.programId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`,
        {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: program,
        }
    );
};

export const DELETE: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const programId = event.params.programId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`,
        {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token },
        }
    );
};
