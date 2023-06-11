import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const comment = await event.request.text();
    const id = await event.params.exerciseId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/exercise/${id}/comment`,
        {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: comment,
        }
    );
};
