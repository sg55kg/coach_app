import type {PageServerLoad} from "./$types";


export const load: PageServerLoad = async ({ cookies, params}) => {
    const accessToken = cookies.get('accessToken')
    const teamId = params.slug

    const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams/${teamId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + accessToken }
    })

    const data = await res.json()

    return {
        team: data
    }
}