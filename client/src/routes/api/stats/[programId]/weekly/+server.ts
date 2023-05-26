import type { RequestHandler } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const programId = event.params.programId;

    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/stats/${programId}/weekly`,
            {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            }
        );
        return new Response(res.body);
    } catch (e) {
        console.log(e);
        throw error(405, 'Could not retrieve stats');
    }
};
