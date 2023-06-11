import type { RequestHandler } from '@sveltejs/kit';
import { error, redirect } from '@sveltejs/kit';

export const POST: RequestHandler = async event => {
    const athleteId = event.params.athleteId;
    const records = await event.request.text();
    const token = event.cookies.get('accessToken');

    return await fetch(
        `${
            import.meta.env.VITE_SERVER_URL
        }api/athletes/${athleteId}/records/list`,
        {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: records,
        }
    );
};
