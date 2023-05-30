import type {RequestHandler} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    const token = event.cookies.get('accessToken')
    return await fetch('http://localhost:8180/api/auth/invite', {
        method: 'POST', body: '{"test":"test"}', headers: { 'Authorization': 'Bearer ' + token }
    })
}