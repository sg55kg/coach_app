import type {Auth0Client} from "@auth0/auth0-spa-js";
import {DisplayProgram, Program} from "../classes/program";
import type {ProgramDTO} from "../classes/program"
import type {User} from "../classes/user";
import {Exercise} from "$lib/classes/program/exercise";
import type {ExerciseComment} from "$lib/classes/program/exercise";
import type {AthleteProgramStatsDTO} from "../classes/program/stats";
import {AthleteProgramStats} from "../classes/program/stats";
import type {Dayjs} from "dayjs";
import type {ExerciseDTO} from "../classes/program/exercise";


export class ProgramService {

    static getProgram = async (id: string) => {

        const res = await fetch(`/api/program/${id}`, {
            method: 'GET',
        })
        const program: ProgramDTO = await res.json()
        return Program.build(program)
    }

    static createProgram = async (program: Program) => {

        let res = await fetch(`/api/program`, {
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

    static deleteProgram = async (programId: string) => {
        const res = await fetch(`/api/program/${programId}`, {
            method: 'DELETE'
        })
        if (res.status > 204) {
            throw new Error('Could not delete program')
        }
    }
    static updateProgram = async (program: Program) => {
        const id = program.id
        console.log(program)
        try {
            const res = await fetch(`/api/program/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(program)
            })
            if (res.status > 299) {
                throw new Error(res.statusText)
            }

            const dto: ProgramDTO = await res.json()
            return Program.build(dto)
        } catch (e) {
            throw new Error('Could not update program')
        }

    }

    static updateExercise = async (exercise: Exercise) => {
        console.log('Updating exercise ' + exercise.id)
        const res = await fetch(`/api/exercise/${exercise.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(exercise)
        })

        const dto: ExerciseDTO = await res.json()
        return Exercise.createFrom(dto)
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

    static getCoachPrograms = async (id: string) => {
        const res = await fetch(`/api/`)
    }

    static searchProgramsByName = async (coachId: string, name: string) => {
        const res = await fetch(`/api/program?coach=${coachId}&name=${name}`, {
            method: 'GET',
        })

        if (res.status > 205) {
            throw new Error('Could not retrieve results')
        }
        const dtos: ProgramDTO[] = await res.json()
        return dtos.map(d => DisplayProgram.build(d))
    }

    static getProgramStats = async (programId: string) => {
        const res = await fetch(`/api/stats/${programId}`, {
            method: 'GET'
        })

        if (res.status > 205) {
            throw new Error('Could not retrieve stats')
        }

        const dto: AthleteProgramStatsDTO = await res.json()
        return AthleteProgramStats.createFrom(dto);
    }

    static getWeeklyProgramStats = async (programId: string) => {
        const res = await fetch(`/api/stats/${programId}/weekly`, {
            method: 'GET'
        })

        if (res.status > 205) {
            throw new Error('Could not retrieve weekly stats')
        }

        const dtos: AthleteProgramStatsDTO[] = await res.json()
        console.log('dtos', dtos)
        return dtos.map(d => AthleteProgramStats.createFrom(d))
    }

    static getDailyProgramStats = async (programId: string, startDate: Dayjs, endDate: Dayjs) => {
        const res = await fetch(`/api/stats/${programId}/daily?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`, {
            method: 'GET'
        })

        if (res.status > 205) {
            throw new Error('Could not retrieve daily stats')
        }

        const dtos: AthleteProgramStatsDTO[] = await res.json()
        return dtos.map(d => AthleteProgramStats.createFrom(d))
    }
}

