import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const userId = event.params.userId;

    try {
        const res = await event.fetch(
            `${import.meta.env.VITE_SERVER_URL}api/chat-rooms/user/${userId}`,
            {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            }
        );
        return new Response(res.body, {
            status: res.status,
            statusText: res.statusText,
        });
    } catch (e) {
        throw error(404, 'Chat rooms could not be found');
    }
};
