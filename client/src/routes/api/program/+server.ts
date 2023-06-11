import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const program = await event.request.json();
    const coachId = program.coachId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`,
        {
            method: 'POST',
            body: JSON.stringify(program),
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
        }
    );
    return new Response(await res.text(), { status: res.status, headers: res.headers, statusText: res.statusText });
};

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const name = event.url.searchParams.get('name');
    const coachId = event.url.searchParams.get('coach');
    const res = await fetch(
        `${
            import.meta.env.VITE_SERVER_URL
        }api/programs/coach/${coachId}/search?name=${name}`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
        }
    );
    return new Response(await res.text(), { status: res.status, headers: res.headers, statusText: res.statusText });
};
