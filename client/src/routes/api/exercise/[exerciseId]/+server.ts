import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const exercise = await event.request.text();
    const id = event.params.exerciseId;

    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        body: exercise,
    });
};

export const DELETE: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const id = event.params.exerciseId;

    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
    });
};
