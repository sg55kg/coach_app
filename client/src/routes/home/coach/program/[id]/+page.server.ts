import type {PageServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";


export const load: PageServerLoad = async ({ params, cookies }) => {
    const programId = params.id

    return { programId }
}