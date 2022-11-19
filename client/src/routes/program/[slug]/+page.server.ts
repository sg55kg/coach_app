import type {PageServerLoad} from "../../../../.svelte-kit/types/src/routes/sverdle/$types";
import {ProgramService} from "../../../lib/server/service/ProgramService";


export const load: PageServerLoad = async ({ params }) => {
    const program = await ProgramService.getProgram(params.slug)
    console.log(program)
    return { program: { ...program } }
}