import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async event => {
    const teamId = event.params.teamId;
    console.log(teamId);
    const token = event.cookies.get('accessToken');
    const res = await event.fetch(
        `${import.meta.env.VITE_SERVER_URL}api/teams/${teamId}/display`,
        {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
        }
    );

    return {
        team: await res.json(),
    };
};
