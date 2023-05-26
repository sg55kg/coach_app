import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const chatId = event.params.chatId;
    const start = event.url.searchParams.get('start');
    const end = event.url.searchParams.get('end');

    try {
        const res = await fetch(
            `${
                import.meta.env.VITE_SERVER_URL
            }api/messages/chat/${chatId}?start=${start}&end=${end}`,
            {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            }
        );
        return new Response(res.body);
    } catch (e) {
        throw error(404, 'Messages not found');
    }
};

export const POST: RequestHandler = async event => {
    const token = import.meta.env.VITE_CF_STREAM_TOKEN;
    const file = event.request.body;

    try {
        const res = await fetch(import.meta.env.VITE_CF_STREAM_URL, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: 'Bearer ' + token,
            },
        });
        return new Response(null);
    } catch (e) {
        throw error(405, 'Could not save video');
    }
};
