import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const teamReq = await event.request.text();

    return await event.fetch(
        `${import.meta.env.VITE_SERVER_URL}api/chat-rooms/`,
        {
            method: 'POST',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: teamReq,
        }
    );
};
