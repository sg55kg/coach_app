import type {LayoutServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";


export const load: LayoutServerLoad = async (event) => {
    if (!event.cookies.get('accessToken') || !event.cookies.get('idToken')) {
        console.log('accessToken')
        event.cookies.delete('accessToken', { path: '/'})
        event.cookies.delete('idToken', { path: '/'})
        throw redirect(307, '/')
    }
}