import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const stripeAccountId = event.params.stripeAccountId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/stripe/${stripeAccountId}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token,
            },
        }
    );
    return new Response(await res.text(), { status: res.status, statusText: res.statusText, headers: res.headers});
};
