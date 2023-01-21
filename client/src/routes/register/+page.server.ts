import type {Actions} from "@sveltejs/kit";


export const actions: Actions = {
    default: async (event) => {
        const data = await event.request.formData()
        const username = data.get('username')
        const email = data.get('email')
        const password = data.get('password')
        const passwordConfirm = data.get('password-confirm')

        try {

        } catch (e) {

        }
    }
}