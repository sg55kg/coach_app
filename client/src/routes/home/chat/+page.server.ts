import type { PageServerLoad } from '../../../../.svelte-kit/types/src/routes/register/$types';
import { redirect } from '@sveltejs/kit';

export const load: PageServerLoad = async event => {
    const token = event.cookies.get('accessToken');

    if (!token) {
        throw redirect(308, '/');
    }

    return { token };
};
