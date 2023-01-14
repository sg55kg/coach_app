import type {Auth0Client} from "@auth0/auth0-spa-js";
import {DisplayProgram, Program} from "../classes/program";
import type {ProgramDTO} from "../classes/program"
import type {User} from "../classes/user";
import type {Exercise} from "$lib/classes/program/exercise";
import type {ExerciseComment} from "$lib/classes/program/exercise";


export class ProgramService {

    static getCoachPrograms = async (client: Auth0Client, coachId: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + accessToken }
        })

        const programsResponse = await res.json()
        console.log('prog res', programsResponse)
        return programsResponse.map((p: ProgramDTO) => DisplayProgram.build(p))
    }

    static getProgram = async (client: Auth0Client, id: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${id}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + accessToken}
        })
        const program: ProgramDTO = await res.json()
        return Program.build(program)
    }

    static createProgram = async (client: Auth0Client, program: Program, user: User) => {
        const accessToken = await client.getTokenSilently()
        const coachId = user?.coachData?.id as string
        console.log(program)

        let res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/coach/${coachId}`, {
            method: 'POST',
            body: JSON.stringify(program),
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' }
        })

        if (res.status > 205) {
            throw new Error('Could not create program')
        }

        const dbProgram: ProgramDTO = await res.json()

        return Program.build(dbProgram)
    }

    static updateCoachPrograms = async (client: Auth0Client, coachData: any) => {
        const accessToken = await client.getTokenSilently()
        const id = coachData.id

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/coach/${id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + accessToken },
            body: JSON.stringify(coachData)
        })

        const programs = await res.json()
        return programs
    }

    static updateProgram = async (client: Auth0Client, program: Program) => {
        const accessToken = await client.getTokenSilently()
        const id = program.id

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/programs/${id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' },
            body: JSON.stringify(program)
        })

        if (res.status > 299) {
            throw new Error('Could not update program')
        }

        const dto: ProgramDTO = await res.json()
        return Program.build(dto)
    }

    static updateExercise = async (client: Auth0Client, exercise: Exercise) => {
        const accessToken = await client.getTokenSilently()
        console.debug(exercise)

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${exercise.id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' },
            body: JSON.stringify(exercise)
        })

        return await res.json()
    }

    static deleteExercise = async (client: Auth0Client, exercise: Exercise) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${exercise.id}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' }
        })

        if (res.status !== 204) {
            throw new Error(`Could not delete exercise ${exercise.name}`)
        }
    }

    static addExerciseComment = async (client: Auth0Client, comment: ExerciseComment) => {
        const accessToken = await client.getTokenSilently()

        console.log(JSON.stringify(comment))
        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/exercise/${comment.exerciseId}/comment`, {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' },
            body: JSON.stringify(comment)
        })

        if (res.status === 404) {
            throw new Error('Could not find the exercise for this comment')
        }
        return await res.json()
    }
}

