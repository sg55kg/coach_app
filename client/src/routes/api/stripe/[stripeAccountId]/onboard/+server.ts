import type {RequestHandler} from "@sveltejs/kit";
import {error, redirect} from "@sveltejs/kit";


export const GET: RequestHandler = async (event) => {
    console.log('GET\n\n')
    const token = await event.cookies.get('accessToken')
    if (!token) {
        throw redirect(302, '/')
    }
    const stripeAccountId = event.params.stripeAccountId
    const returnUrl = event.url.searchParams.get('returnUrl')

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/stripe/${stripeAccountId}/onboard?returnUrl=${returnUrl}`, {
            method: 'GET',
            headers: { 'Content-Type': 'text/html; charset=utf-8', 'Authorization': 'Bearer ' + token }
        })

        return new Response(res.body)
    } catch (e: any) {
        throw error(404, e.message)
    }
}