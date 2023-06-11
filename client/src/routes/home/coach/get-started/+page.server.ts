import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
    default: async event => {
        const data = await event.request.formData();
        const newCoach = {};

        try {
        } catch (e) {}
    },
};
