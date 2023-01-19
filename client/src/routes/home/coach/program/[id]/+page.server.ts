import type {PageServerLoad} from "./$types";
import {error} from "@sveltejs/kit";


export const load: PageServerLoad = async ({ params, cookies, fetch }) => {
    const token = cookies.get('accessToken')
    const programId = params.id

    try {
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`, {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
        })
        console.log(res.status)
        console.log(res.statusText)
        const programData = await res.json()
        return { programDto: programData }
    } catch (e) {
        console.log(e)
        throw error(404, 'Could not find program')
    }
}