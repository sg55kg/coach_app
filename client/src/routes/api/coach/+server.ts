import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const newCoach = await event.request.text();
    const token = event.cookies.get('accessToken');

    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/users/coach`, {
        method: 'PUT',
        headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: newCoach,
    });
};
