import type { RequestHandler } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const user = await event.request.text();
    const userId = event.params.userId;

    return await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/users/${userId}`,
        {
            method: 'PUT',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: user,
        }
    );
};
