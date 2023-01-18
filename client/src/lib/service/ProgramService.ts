import type {Auth0Client} from "@auth0/auth0-spa-js";
import {DisplayProgram, Program} from "../classes/program";
import type {ProgramDTO} from "../classes/program"
import type {User} from "../classes/user";
import type {Exercise} from "$lib/classes/program/exercise";
import type {ExerciseComment} from "$lib/classes/program/exercise";


export class ProgramService {

    static getProgram = async (id: string) => {

        const res = await fetch(`/api/programs/${id}`, {
            method: 'GET',
        })
        const program: ProgramDTO = await res.json()
        return Program.build(program)
    }

    static createProgram = async (program: Program, user: User) => {
        const coachId = user?.coachData?.id as string

        let res = await fetch(`/api/programs/coach/${coachId}`, {
            method: 'POST',
            body: JSON.stringify(program),
            headers: { 'Content-Type': 'application/json' }
        })

        if (res.status > 205) {
            throw new Error('Could not create program')
        }

        const dbProgram: ProgramDTO = await res.json()

        return Program.build(dbProgram)
    }

    static updateProgram = async (program: Program) => {
        const id = program.id

        const res = await fetch(`/api/programs/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(program)
        })

        if (res.status > 299) {
            throw new Error('Could not update program')
        }

        const dto: ProgramDTO = await res.json()
        return Program.build(dto)
    }

    static updateExercise = async (exercise: Exercise) => {

        const res = await fetch(`/api/exercise/${exercise.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(exercise)
        })

        return await res.json()
    }

    static deleteExercise = async (exercise: Exercise) => {

        const res = await fetch(`/api/exercise/${exercise.id}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        })

        if (res.status !== 204) {
            throw new Error(`Could not delete exercise ${exercise.name}`)
        }
    }

    static addExerciseComment = async (comment: ExerciseComment) => {
        const res = await fetch(`/api/exercise/${comment.exerciseId}/comment`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(comment)
        })

        if (res.status === 404) {
            throw new Error('Could not find the exercise for this comment')
        }
        return await res.json()
    }
}

