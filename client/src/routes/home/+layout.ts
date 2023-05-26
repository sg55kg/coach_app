import type { LayoutLoad } from './$types';
import { redirect } from '@sveltejs/kit';

export const prerender = false;

export const load: LayoutLoad = async event => {
    const code = event.url.searchParams.get('code');

    if (code) {
        try {
            const res = await event.fetch('/api/auth/token', {
                method: 'POST',
                body: JSON.stringify({ code }),
            });
            const { user, userData } = await res.json();
            return { user, userData };
        } catch (e) {
            throw redirect(302, '/');
        }
    }
};
