import { error, type RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async event => {
    const coachId = event.params.coachId;
    const teamId = event.params.teamId;
    const token = event.cookies.get('accessToken');

    try {
        return new Response();
    } catch (e) {
        throw error(404, 'Could not retrieve coach programs');
    }
};
