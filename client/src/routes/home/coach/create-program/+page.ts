import type { PageLoad } from './$types';

export const load: PageLoad = event => {
    const athleteId = event.url.searchParams.get('athlete');

    if (athleteId) {
        return { athleteId };
    }
};
