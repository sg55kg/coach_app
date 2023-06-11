import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const inviteUserRequest = await event.request.text();
    return await fetch(`${import.meta.env.VITE_SERVER_URL}api/auth/invite`, {
        method: 'POST',
        body: inviteUserRequest,
        headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
    });
};
