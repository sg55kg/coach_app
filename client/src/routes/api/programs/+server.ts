import type {RequestHandler, RequestEvent} from "./$types";
import axios from "axios";


export const POST: RequestHandler = async ({ request, cookies }) => {
    const program = await request.json()
    const token = cookies.get('Authority')
    //const refresh = cookies.get('refresh_token')
   // console.log(token)
    console.log(program)

    try {
        //const res = await axios.post('', program)
        const res = await fetch('http://localhost:8180/api/programs', {
            method: 'POST',
            headers: new Headers({
                'content-type':'application/json',
                'authority': 'Bearer ' + token!,
            }),
            body: JSON.stringify(program)
        })
        const savedProgram = await res.json()
        console.log('saved program', savedProgram)
        return new Response(savedProgram)
    } catch (e) {
        console.log(e)
        return Error(e.message)
    }
}

export const PUT: RequestHandler = async ({ request, cookies }) => {
    const program = await request.json()
    const token = cookies.get('Authority')
    console.log(program.id)
    try {
        const res = await fetch(`http://localhost:8180/api/programs/${program.id}`, {
            method: 'PUT',
            headers: new Headers({
                'content-type': 'application/json',
                'authority': 'Bearer ' + token,

            }),
            body: JSON.stringify(program)
        })

        const updatedProgram = await res.json()
        return new Response(JSON.stringify(updatedProgram))
    } catch (e) {
        return Error(e.message)
    }
}

export const GET: RequestHandler = async ({ request }) => {
    const headers = request.headers
    const token = headers.get('Authority')

    try {
        const res = await fetch(`http://localhost:8180/api/programs`, {
            method: 'GET',
            headers: { 'Authority': 'Bearer ' + token }
        })

        //const other = await res.text()
        //console.log(other)
        const resData = await res.json()
        console.log('this is new', resData)

        return { programs: resData }
    } catch (e) {
        console.log(e)
    }
}