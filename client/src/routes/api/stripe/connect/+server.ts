import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const newTeamFinanceRequest = await event.request.text();

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/stripe/connect`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
        body: newTeamFinanceRequest,
    });
    return new Response(await res.text(), { status: res.status, statusText: res.statusText });
};
