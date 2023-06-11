import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = await event.cookies.get('accessToken');
    const stripeAccountId = event.params.stripeAccountId;
    const returnUrl = event.url.searchParams.get('returnUrl');

    const res = await fetch(
        `${
            import.meta.env.VITE_SERVER_URL
        }api/stripe/${stripeAccountId}/onboard?returnUrl=${returnUrl}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'text/html; charset=utf-8',
                'Authorization': 'Bearer ' + token,
            },
        }
    );
    return new Response(await res.text(), { status: res.status, statusText: res.statusText });
};
