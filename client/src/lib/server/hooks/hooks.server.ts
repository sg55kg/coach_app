import type {Handle} from "@sveltejs/kit";
import {redirect} from "@sveltejs/kit";


export const handle: Handle = async ({ event, resolve }) => {
    const token = event.cookies.get('accessToken')

    if (!token) {
        throw redirect(302, '/')
    }

    const response = await resolve(event)
    return response
}