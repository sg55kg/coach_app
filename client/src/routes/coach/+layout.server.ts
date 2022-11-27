import type {LayoutServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";

export const load: LayoutServerLoad = async ({ cookies, params }) => {
    if(!cookies.get('Authority') && !cookies.get('refresh_token')) {
        throw redirect(302, '/login')
    }

    if(cookies.get('Authority') && params.slug !== undefined) {
        const userId = params.slug
        return { userId }
    }
}