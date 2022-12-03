import type {PageServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";


export const load: PageServerLoad = async ({ params, cookies }) => {
    const programId = params.id
    const accessToken = cookies.get('Authority')

    if(accessToken === undefined || accessToken.includes('undefined')) {
        throw redirect(308, '/login')
    }

    try {
        const res = await fetch(`http://localhost:8180/api/programs/${programId}`, {
            method: 'GET',
            headers: new Headers({
                'content-type': 'application/json',
                'Authority': 'Bearer ' + cookies.get('Authority')
            })
        })
        const result = await res.json()
        console.log(result)
        return { programResponse: result }
    } catch (e) {
        console.log(e)
    }
}