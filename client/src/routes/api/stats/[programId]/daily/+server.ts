import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const token = event.cookies.get('accessToken');
    const programId = event.params.programId;
    const startDate = event.url.searchParams.get('startDate');
    const endDate = event.url.searchParams.get('endDate');

    return await fetch(
        `${
            import.meta.env.VITE_SERVER_URL
        }api/stats/${programId}/daily?startDate=${startDate}&endDate=${endDate}`,
        {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
        }
    );
};
