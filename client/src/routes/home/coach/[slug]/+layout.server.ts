import type {LayoutServerLoad} from './$types'
import {redirect} from "@sveltejs/kit";

export const load: LayoutServerLoad = async ({ cookies, params, url }) => {
    const token = cookies.get('accessToken')
    const coachId = params.slug

    if (!token) {
         throw redirect(302, '/')
    }
    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + token }
        })

        const programsResponse = await res.json()
        return {
            programs: programsResponse
        }
    } catch (e) {
        console.log(e)
    }
    return {}

}