import type { RequestHandler } from '@sveltejs/kit';

export const PUT: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const program = await event.request.text();
    const programId = event.params.programId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`,
        {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: program,
        }
    );
    return new Response(await res.text(), { status: res.status, headers: res.headers, statusText: res.statusText });
};

export const DELETE: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const programId = event.params.programId;

    const res = await fetch(
        `${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`,
        {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token },
        }
    );
    return new Response(await res.text(), { status: res.status, headers: res.headers, statusText: res.statusText });
};
