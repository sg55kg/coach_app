import type { PageServerLoad } from "../../../../.svelte-kit/types/src/routes/login/$types";
import {ProgramService} from "../../../lib/server/service/ProgramService";


export const load: PageServerLoad = async ({ params }) => {
    const programs = await ProgramService.getPrograms()

    return { programs }
}