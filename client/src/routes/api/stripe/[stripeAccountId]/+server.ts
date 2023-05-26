import type { RequestHandler } from '@sveltejs/kit';
import { error, redirect } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    if (!token) {
        throw redirect(302, '/');
    }
    const stripeAccountId = event.params.stripeAccountId;
    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/stripe/${stripeAccountId}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: 'Bearer ' + token,
                },
            }
        );
        return new Response(res.body);
    } catch (e: any) {
        throw error(404, e.message);
    }
};
