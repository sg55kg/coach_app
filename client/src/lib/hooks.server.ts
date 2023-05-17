import type {Handle, HandleFetch} from "@sveltejs/kit";
import {redirect} from "@sveltejs/kit";


export const handle: Handle = async ({ event, resolve }) => {
    const token = event.cookies.get('accessToken')
    console.log('fired handle')

    if (!token) {
        throw redirect(302, '/')
    }

    return await resolve(event)
}

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    const token = event.cookies.get('accessToken')
    console.log('fired handle fetch')

    if (!token) {
        throw redirect(302, '/')
    }

    return await fetch(request)
}