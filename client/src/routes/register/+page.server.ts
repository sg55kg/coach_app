import type {Action, Actions} from "@sveltejs/kit";
import {redirect} from "@sveltejs/kit";
import {z} from 'zod'
import type {ZodError} from 'zod'

const registerSchema = z.object({
    username: z
        .string({ required_error: 'Username is required' })
        .min(3, { message: 'Username must be at least 3 characters' })
        .max(30)
        .trim(),
    password: z
        .string({ required_error: 'Password is required' })
        .min(8, { message: 'Password must be at least 8 characters' }),
    passwordConfirm: z
        .string({ required_error: 'Password confirmation is required' }),
    email: z
        .string({ required_error: 'Email is required' })
        .min(1)
        .max(50)
        .email({ message: 'Must be a valid email address' }),
    firstName: z
        .string({ required_error: 'First name is required' })
        .min(1)
        .max(50)
        .trim(),
    lastName: z
        .string({ required_error: 'Last name is required' })
        .min(1)
        .max(50)
        .trim()
}).superRefine(({ password, passwordConfirm }, ctx) => {
    if (password !== passwordConfirm) {
        ctx.addIssue({
            code: 'custom',
            message: 'Passwords must be matching',
            path: ['passwordConfirm']
        })
    }
})

interface RegisterData extends FormData {
    username: string,
    password: string,
    passwordConfirm: string,
    email: string,
    firstName: string,
    lastName: string,
}

export const actions: Actions = {
    default: async ({ request }) => {

        const data = Object.fromEntries(await request.formData())
        try {
            const validated = registerSchema.parse(data)
        } catch (e: any) {
            console.log(e.errors[0].path)
            const { fieldErrors: errors } = (e as ZodError).flatten()
            const { password, passwordConfirm, ...rest } = data
            console.log(errors)
            return {
                data: rest,
                errors
            }
        }
        try {
            const res = await fetch(`http://localhost:8180/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: data['username'], //username,
                    password: data['password'],
                    firstName: data['first-name'],
                    lastName: ['last-name'],
                    email: data['email'],
                    role: 'athlete'
                })
            })
        } catch (e) {
            return { error: e}
        }
        throw redirect(303, '/login')
    }
}