import type {RequestHandler} from "@sveltejs/kit";
import {json, redirect} from "@sveltejs/kit";


export const POST: RequestHandler = async (event) => {
    event.cookies.delete('accessToken', { path: '/' })
    event.cookies.delete('idToken', { path: '/' })
    return json({
        status: 200,
        redirectUrl: 'https://dev-iubbkos4gue16ad5.us.auth0.com/v2/logout?client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&returnTo=https://localhost:5173'
    })
   // return { status: 200, redirectUrl: 'https://dev-iubbkos4gue16ad5.us.auth0.com/v2/logout?client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&returnTo=https://localhost:5173' }
    //throw redirect(302, 'https://dev-iubbkos4gue16ad5.us.auth0.com/v2/logout?client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&returnTo=https://localhost:5173')
}