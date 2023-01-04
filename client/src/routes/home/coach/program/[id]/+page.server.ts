import type {PageServerLoad} from "./$types";


export const load: PageServerLoad = async ({ params, cookies }) => {
    const programId = params.id
    console.log(programId)

    return { programId }
}