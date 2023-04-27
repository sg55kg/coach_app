import {redirect} from "@sveltejs/kit";

export const reqPost = async (url: string, body: any, token: string | undefined, type: string = 'application/json') => {
    if (!token) {
        throw redirect(302, '/')
    }
    const res = await fetch(url, {
        method: 'POST',
        headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': type },
        body
    })
    return res
}

export const reqGet = async (url: string, token: string | undefined, type: string = 'application/json') => {
    if (!token) {
        throw redirect(302, '/')
    }
    const res = await fetch(url, {
        method: 'GET',
        headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': type }
    })
    return res
}

export const reqPut = async () => {

}

export const reqDelete = async () => {

}