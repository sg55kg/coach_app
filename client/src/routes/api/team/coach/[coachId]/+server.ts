import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const coachId = event.params.coachId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`,
        {
            method: 'GET',
            headers: { Authorization: 'Bearer ' + token },
        }
    );
};
