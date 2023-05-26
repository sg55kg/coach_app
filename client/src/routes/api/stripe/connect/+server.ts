import type { RequestHandler } from '@sveltejs/kit';
import { error, redirect } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    if (!token) {
        throw redirect(302, '/');
    }

    const newTeamFinanceRequest = await event.request.json();
    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/stripe/connect`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: 'Bearer ' + token,
                },
                body: JSON.stringify(newTeamFinanceRequest),
            }
        );

        return new Response(res.body);
    } catch (e: any) {
        throw error(405, e.message);
    }
};
