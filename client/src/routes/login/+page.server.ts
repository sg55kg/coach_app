import type {Actions} from "@sveltejs/kit";


export const actions: Actions = {
    default: async ({ request }) => {
        const data = await request.formData()
        let result
        try {
            const res = await fetch(`http://localhost:8180/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: data.get('username'),
                    password: data.get('password')
                })
            })
            console.log(res)
            result = await res.json()
        } catch (e) {
            return { error: e }
        }
        return { success: result, status: 200 }
    }
}