import type { PageServerLoad } from "../../../../.svelte-kit/types/src/routes/login/$types";
import {ProgramService} from "../../../lib/server/service/ProgramService";


export const load: PageServerLoad = async ({ params, cookies }) => {

    const res = await fetch(`http://localhost:8180/api/programs`, {
        method: 'GET',
       // credentials: 'include',
        headers: {
            'Authorization': cookies.get('Authority')!
        }
    })

    const programs = await res.json()
    return { programs }
}