import {error} from "@sveltejs/kit";
import type {LayoutServerLoad} from "../../../../../../.svelte-kit/types/src/routes/$types";

export const load: LayoutServerLoad = async (event) => {
    const programId = event.params.id;
    const token = event.cookies.get('accessToken');
    try {
        const res = await fetch(
            `${import.meta.env.VITE_SERVER_URL}api/programs/${programId}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
            }
        );
        const programData = await res.json();
        return { programDto: programData };
    } catch (e) {
        console.log(e);
        throw error(404, 'Could not find program');
    }}