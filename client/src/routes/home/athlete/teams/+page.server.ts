import type {PageServerLoad} from "./$types";
import {error, redirect} from "@sveltejs/kit";


export const load: PageServerLoad = async (event) => {
    const token = event.cookies.get('accessToken')
    if (!token) {
        throw redirect(307, '/')
    }
    try {
        const res = await event.fetch(`${import.meta.env.VITE_SERVER_URL}api/teams/`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token }
        })

        const teams = await res.json()
        return { teams }
    } catch (e) {
        throw error(404, 'Could not retrieve teams')
    }
}