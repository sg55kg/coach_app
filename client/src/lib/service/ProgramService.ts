import type {Auth0Client} from "@auth0/auth0-spa-js";
import {DisplayProgram, Program} from "../classes/program";
import type {ProgramDTO} from "../classes/program"
import type {User} from "../classes/user";


export class ProgramService {

    static getCoachPrograms = async (client: Auth0Client, coachId: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`http://localhost:8180/api/programs/coach/${coachId}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + accessToken }
        })

        const programsResponse = await res.json()
        console.log(programsResponse)
        return programsResponse.map((p: ProgramDTO) => DisplayProgram.build(p))
    }

    static getProgram = async (client: Auth0Client, id: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`http://localhost:8180/api/programs/${id}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + accessToken}
        })
        return await res.json()
    }

    static createProgram = async (client: Auth0Client, program: Program, user: User) => {
        const accessToken = await client.getTokenSilently()
        const coachId = user?.coachData?.id as string
        console.log(program)

        let res = await fetch(`http://localhost:8180/api/programs/coach/${coachId}`, {
            method: 'POST',
            body: JSON.stringify(program),
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' }
        })

        if (res.status > 205) return

        const dbProgram = await res.json()

        if (program.isCurrent) {
            res = await fetch(`http://localhost:8180/api/athletes/${program.athleteId}/currentProgram/${dbProgram.id}`, {
                method: 'PUT',
                headers: { 'Authorization': 'Bearer ' + accessToken }
            })

            const athleteData = await res.json()
            console.log(athleteData)
        }

        return dbProgram
    }

    static updateCoachPrograms = async (client: Auth0Client, coachData: any) => {
        const accessToken = await client.getTokenSilently()
        const id = coachData.id

        const res = await fetch(`http://localhost:8180/api/coach/${id}`, {
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

        const res = await fetch(`http://localhost:8180/api/programs/${id}`, {
            method: 'PUT',
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' },
            body: JSON.stringify(program)
        })

        return await res.json()
    }
}

