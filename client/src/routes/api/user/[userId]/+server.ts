import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const user = await event.request.json();

    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/users/${user.id}`,
            {
                method: 'PUT',
                headers: {
                    Authorization: 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user),
            }
        );
        const userData = await res.json();
        return new Response(JSON.stringify(userData));
    } catch (e) {
        throw error(405, 'Could not update user');
    }
};
