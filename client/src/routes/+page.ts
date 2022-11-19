import type {PageServerLoad} from "../../.svelte-kit/types/src/routes/sverdle/$types";
import {ProgramService} from "../lib/ProgramService";

export const load: PageServerLoad = async ({ fetch, params }) => {
    const programs = await ProgramService.getPrograms()

    return { programs }
}
