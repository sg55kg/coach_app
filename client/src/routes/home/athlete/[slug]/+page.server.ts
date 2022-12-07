import type {PageServerLoad} from "../../../../../.svelte-kit/types/src/routes/login/$types";


export const load: PageServerLoad = async ({ params, url }) => {
    console.log(url.searchParams)
    console.log(url.pathname)
    //@ts-ignore
    console.log(params.slug)
}