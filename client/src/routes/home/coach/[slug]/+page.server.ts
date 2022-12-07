import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ params, cookies }) => {

    const res = await fetch(`http://localhost:8180/api/programs`, {
        method: 'GET',
       // credentials: 'include',
        headers: {
            'Authorization': "Bearer " + cookies.get('Authority')!
        }
    })
    console.log(res)
    const programs = await res.json()
    return { programs }
}