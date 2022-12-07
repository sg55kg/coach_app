import type {LayoutServerLoad} from "./$types";
import {redirect} from "@sveltejs/kit";

export const load: LayoutServerLoad = async ({ cookies, params }) => {
    // const token = cookies.get('Authority')
    // console.log(token)
    // try {
    //     const res = await fetch(`http://localhost:8180/api/programs`, {
    //         method: 'GET',
    //         headers: { 'Authority': 'Bearer ' + token! }
    //     })
    //     console.log(res)
    //     const resData = await res.json()
    //     console.log('this is new', resData)
    //     return { programs: resData }
    // } catch (e) {
    //     console.log(e)
    // }

}